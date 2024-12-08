package ar.uba.fi.ingsoft1.product.users;

public record UserDTO(
        String name,
        String surname,
        String profilePicture,
        String gender,
        String age,
        String address
) {
    public UserDTO(User user) {
        this(user.getName(), user.getSurname(), user.getProfilePicture(), user.getGender(), user.getAge(), user.getAddress());
    }
}
