package ar.uba.fi.ingsoft1.product.users;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`user`")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "username")
    private String name;

    @Column(nullable = false, name = "surname")
    private String surname;

    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(nullable = false, name = "role")
    private String role;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "gender")
    private String gender;

    @Column(nullable = false, name = "age")
    private String age;

    @Column(nullable = false, name = "address")
    private String address;

    public User(String name, String surname, String password, String email, String profilePicture, String role, String gender, String age, String address) {
        setName(name);
        setSurname(surname);
        setEmail(email);
        setProfilePicture(profilePicture);
        setPassword(password);
        setRole(role);
        setGender(gender);
        setAge(age);
        setAddress(address);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
