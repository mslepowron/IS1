package ar.uba.fi.ingsoft1.product.users;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
class UserRestController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestHeader("Authorization") String authorizationHeader) {
        return userService.getUserByEmail(authorizationHeader)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/")
    public Optional<UserDTO> updateUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @NonNull @RequestBody UserUpdateDTO data
    ) {
        return userService.updateUser(authorizationHeader, data);
    }

}
