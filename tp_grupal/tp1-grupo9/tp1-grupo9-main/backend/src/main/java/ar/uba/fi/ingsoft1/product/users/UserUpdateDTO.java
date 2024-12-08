package ar.uba.fi.ingsoft1.product.users;

import java.util.Optional;

record UserUpdateDTO(
        Optional<String> name,
        Optional<String> surname,
        Optional<String> profilePicture,
        Optional<String> gender,
        Optional<String> age,
        Optional<String> address
) {
    public User applyTo(User user) {
        name.ifPresent(user::setName);
        surname.ifPresent(user::setSurname);
        profilePicture.ifPresent(user::setProfilePicture);
        gender.ifPresent(user::setGender);
        age.ifPresent(user::setAge);
        address.ifPresent(user::setAddress);
        return user;
    }
}
