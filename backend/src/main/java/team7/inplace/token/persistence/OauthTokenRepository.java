package team7.inplace.token.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.OauthToken;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {

    Optional<OauthToken> findByUserId(Long userId);
}
