package team7.inplace.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"USER\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password, String nickname, UserType userType, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userType = userType;
        this.role = role;
    }

    public void updateInfo(String nickname) {
        this.nickname = nickname;
    }
}
