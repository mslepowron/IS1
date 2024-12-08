package ar.uba.fi.ingsoft1.product.services;

import ar.uba.fi.ingsoft1.product.users.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public User signup(UserCreateDTO input) {
        if (userRepository.findByEmail(input.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User(
                input.name(),
                input.surname(),
                passwordEncoder.encode(input.password()),
                input.email(),
                input.profilePicture(),
                String.valueOf(Role.USER),
                input.gender(),
                input.age(),
                input.address());

        String customerEmail = input.email();
        if (customerEmail.isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty");
        }
        String subject = "Hi " + input.name();
        String bodyContent = "Thank you for registering";

        emailService.sendEmail(customerEmail, subject, bodyContent);

        return userRepository.save(user);
    }

    public User authenticate(UserLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );

        return userRepository.findByEmail(input.email())
                .orElseThrow();
    }
}
