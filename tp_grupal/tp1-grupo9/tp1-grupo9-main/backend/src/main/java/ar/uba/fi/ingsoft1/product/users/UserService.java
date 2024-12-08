package ar.uba.fi.ingsoft1.product.users;

import java.util.Optional;

import ar.uba.fi.ingsoft1.product.services.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private String getEmail(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return jwtService.extractClaim(token, Claims::getSubject);
    }

    public Optional<UserDTO> getUserByEmail(@RequestHeader("Authorization") String authorizationHeader) {
        return userRepository.findByEmail(getEmail(authorizationHeader)).map(UserDTO::new);
    }

    public Optional<UserDTO> updateUser(@RequestHeader("Authorization") String authorizationHeader, UserUpdateDTO update) {
        return userRepository.findByEmail(getEmail(authorizationHeader))
                .map(update::applyTo)
                .map(userRepository::save)
                .map(UserDTO::new);
    }
}
