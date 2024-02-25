package com.example.assignment1_1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true)
})
public class AppUser implements UserDetails {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    @Length(min = 2, message = "First name must be at least 2 characters long")
    private String firstName;

    @Getter
    @Setter
    @Column(nullable = false)
    @Length(min = 2, message = "Last name must be at least 2 characters long")
    private String lastName;

    @Getter
    @Setter
    @Column(nullable = false)
    @Length(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    @Email(message = "A valid email is mandatory")
    private String email;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    @Length(min = 3, message = "Username must be at least 3 characters long")
    private String username;

    @Getter
    @Setter
    @Column(nullable = false)
    private String role = Role.ROLE_REGULAR.toString();

    @Getter
    @Setter
    @Length(min = 3, message = "Location must be at least 3 characters long")
    private String location;

    @Getter
    @Setter
    @Column
    private String accountStatus = AccountStatus.PENDING.toString();

    @OneToMany(targetEntity = Dealership.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("author")
    @Getter
    @Setter
    private List<Dealership> dealerships;

    @OneToMany(targetEntity = Car.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("author")
    @Getter
    @Setter
    private List<Car> cars;

    @OneToMany(targetEntity = Employee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("author")
    @Getter
    @Setter
    private List<Employee> employees;

    @OneToMany(targetEntity = ShippingContract.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("author")
    @Getter
    @Setter
    private List<ShippingContract> shippingContracts;

    @OneToMany(targetEntity = Supplier.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("author")
    @Getter
    @Setter
    private List<Supplier> suppliers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(accountStatus, AccountStatus.ACTIVE.toString());
    }
}
