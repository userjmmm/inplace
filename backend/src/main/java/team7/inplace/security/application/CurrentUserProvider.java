package team7.inplace.security.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.persistence.UserJpaRepository;
import user.User;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserJpaRepository userJpaRepository;

    public User getCurrentUser() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        return userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
    }
}
