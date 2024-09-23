package team7.inplace.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
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

    public User(String username, String password, String nickname, UserType userType) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userType = userType;
    }

    protected User() {
    }

    public void updateInfo(String nickname) {
        this.nickname = nickname;
    }
}
