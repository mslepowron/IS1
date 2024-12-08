package ar.uba.fi.ingsoft1.product.orders;

import ar.uba.fi.ingsoft1.product.orders.states.*;
import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.ProductRepository;
import ar.uba.fi.ingsoft1.product.rules.service.RuleService;
import ar.uba.fi.ingsoft1.product.services.EmailService;
import ar.uba.fi.ingsoft1.product.services.JwtService;
import ar.uba.fi.ingsoft1.product.users.Role;
import ar.uba.fi.ingsoft1.product.users.User;
import ar.uba.fi.ingsoft1.product.users.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
class OrderService {

    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RuleService ruleService;
    private final EmailService emailService;
    private final StateFactory stateFactory = new StateFactory();
    private final UserRepository userRepository;

    private String getEmail(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        return jwtService.extractClaim(token, Claims::getSubject);
    }

    public List<OrderDTO> getOrdersByEmail(@RequestHeader("Authorization") String authorizationHeader) {
        return orderRepository
                .findAllByEmail(getEmail(authorizationHeader))
                .stream()
                .map(OrderDTO::new)
                .toList();
    }

    private String getRole(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return jwtService.extractClaim(token, claims -> claims.get("role", String.class));
    }

    public List<OrderDTO> getOrdersByState(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(required = false) String state) {

        if(getRole(authorizationHeader).equals(Role.USER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");
        }

        List<Order> orders = (state != null)
                ? orderRepository.findAllByState(States.valueOf(state.toUpperCase()))
                : orderRepository.findAll();

        return orders.stream()
                .map(OrderDTO::new)
                .toList();
    }

    public Optional<OrderDTO> getOrderById(long id) {
        return orderRepository.findById(id).map(OrderDTO::new);
    }

    private List<Product> createProducts(OrderCreateDTO data) {

        Map<Long, Integer> counts = data.ids().stream()
                .collect(Collectors.groupingBy(
                        id -> id,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));

        return counts.keySet().stream()
                .map(productRepository::findById)
                .map(optional -> optional.orElseThrow(IndexOutOfBoundsException::new))
                .flatMap(product -> Stream.generate(() -> product)
                        .limit(counts.get(product.getId())))
                .collect(Collectors.toList());
    }

    private void createEmail(String customerEmail, Order savedOrder) {
        if (customerEmail.isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty");
        }
        String subject = "Order Confirmation #" + savedOrder.getId();
        String bodyContent = "Thank you for your order. Here are the details:\n" + emailService.generateEmailBodyOrder(savedOrder.getProductsJson());
        emailService.sendEmail(customerEmail, subject, bodyContent);
    }

    public OrderDTO createOrder(@RequestHeader("Authorization") String authorizationHeader, OrderCreateDTO data) {

        List<Product> products = createProducts(data);

        ruleService.validateOrder(products);

        String customerEmail = getEmail(authorizationHeader);
        Optional<User> userByEmail = userRepository.findByEmail(customerEmail);
        String username = userByEmail.map(user -> user.getName() + " " + user.getSurname())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Order order = data.asOrder(products, Confirmed.getStatus(), customerEmail, username);

        State state = stateFactory.getInitialState(OrderStates.STATES.get(States.CONFIRMED.toString()));
        state.stockManager(products, productRepository);
        Order savedOrder = orderRepository.save(order);

        createEmail(customerEmail, savedOrder);

        return new OrderDTO(savedOrder);
    }

    public Optional<OrderDTO> updateOrder(@RequestHeader("Authorization") String authorizationHeader, Long id, OrderUpdateDTO update) {

        getOrderById(id).ifPresent(order -> {

            if (!order.email().equals(getEmail(authorizationHeader)) && Role.USER.toString().equals(getRole(authorizationHeader))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");
            }


            update.getState().ifPresent(stateUpdateDTO -> {

                State currentState = stateFactory.getInitialState(OrderStates.STATES.get(order.state().name()));

                States updatedState = currentState.switchState(
                        order,
                        OrderStates.STATES.get(stateUpdateDTO.name()),
                        getRole(authorizationHeader)
                );

                stateFactory.getInitialState(OrderStates.STATES.get(stateUpdateDTO.name()))
                        .stockManager(order.products(), productRepository);

                update.setState(Optional.of(updatedState));
            });
        });

        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) { throw new IndexOutOfBoundsException(); }

        return orderOptional
                .map(update::applyTo)
                .map(orderRepository::save)
                .map(OrderDTO::new);
    }

}
