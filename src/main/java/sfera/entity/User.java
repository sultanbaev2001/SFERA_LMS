package sfera.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sfera.entity.enums.ERole;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;
    @Enumerated(EnumType.STRING)
    private ERole role;
    @OneToOne
    private Contact contact;

    private boolean active;


}
