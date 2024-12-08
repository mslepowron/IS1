package ar.uba.fi.ingsoft1.product.orders;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
class OrderRestController {
    private final OrderService orderService;

    @GetMapping("/")
    public List<OrderDTO> getOrdersByEmail(@RequestHeader("Authorization") String authorizationHeader) {
        return orderService.getOrdersByEmail(authorizationHeader);
    }

    @GetMapping("/state/")
    public List<OrderDTO> getOrdersByState(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(required = false) String state) {
        return orderService.getOrdersByState(authorizationHeader, state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @NonNull @RequestBody OrderCreateDTO data
    ) {
        try {
            return orderService.createOrder(authorizationHeader, data);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (RuleException e) {
            throw new RuleException(e.getMessage());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/order/")
    public Optional<OrderDTO> updateOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam() Long id,
            @NonNull @RequestBody OrderUpdateDTO data
    ) {
        return orderService.updateOrder(authorizationHeader, id, data);
    }
}
