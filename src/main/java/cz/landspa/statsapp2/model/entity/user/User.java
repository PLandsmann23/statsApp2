package cz.landspa.statsapp2.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.landspa.statsapp2.model.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "UniqueUsername", columnNames = {"username"}),@UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // @Column(unique = true)
    @NotBlank(message = "Uživatelské jméno musí být vyplněno")
    String username;

    // @Column(unique = true)
    @NotBlank(message = "Email musí být vyplněn")
    String email;



    // @JsonIgnore
    @NotBlank(message = "Heslo musí být vyplněno")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;


    @Column(columnDefinition = "ENUM('USER','ADMIN')")
    @Enumerated(EnumType.STRING)
    Role role = Role.USER;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    UserSetting userSetting;

    @Column(nullable = false)
    boolean enabled = false;



}
