package team7.inplace.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.persistence.UserRepository;

@RestController("/temp")
@RequiredArgsConstructor
public class TempController {
    private final UserRepository userRepository;

    @DeleteMapping
    public void deleteId() {
        var userId = AuthorizationUtil.getUserId();

        userRepository.deleteById(userId);
    }
}
