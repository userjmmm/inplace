package team7.inplace.likedPlace.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.config.annotation.CustomRepositoryTest;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.LikedPlaceErrorCode;
import team7.inplace.likedPlace.domain.LikedPlace;
import team7.inplace.place.domain.Place;
import team7.inplace.user.domain.Role;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserType;

@CustomRepositoryTest
@Transactional
class LikedPlaceRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LikedPlaceRepository likedPlaceRepository;

    private User user1;
    private Place place1;

    @BeforeEach
    public void init() {
        // 유저 생성 및 persist
        user1 = new User("user1", "pass1", "nick1", UserType.KAKAO, Role.USER);
        entityManager.persist(user1);

        // 장소 생성 및 persist
        place1 = new Place("Place 1",
            "\"wifi\": true, \"pet\": false, \"parking\": false, \"forDisabled\": true, \"nursery\": false, \"smokingRoom\": false}",
            "menuImg.url", "카페",
            "Address 1|Address 2|Address 3",
            "10.0", "10.0",
            Arrays.asList("한글날|수|N", "크리스마스|수|Y"),
            Arrays.asList("오픈 시간|9:00 AM|월", "닫는 시간|6:00 PM|월"),
            Arrays.asList("삼겹살|5000|false|menu.url|description",
                "돼지찌개|7000|true|menu.url|description"),
            LocalDateTime.of(2024, 3, 28, 5, 30),
            Arrays.asList(
                "menuBoard1.url",
                "menuBoard2.url"
            )
        );
        entityManager.persist(place1);

        // EntityManager flush 및 clear
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("FindByUserIdAndPlaceId 테스트")
    public void test1() {
        // given
        LikedPlace likedPlace = new LikedPlace(user1, place1);
        entityManager.persist(likedPlace);

        // when
        Optional<LikedPlace> actual = likedPlaceRepository.findByUserIdAndPlaceId(
            user1.getId(), place1.getId()
        );

        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().getUser().getUsername()).isEqualTo(user1.getUsername());
        assertThat(actual.get().getPlace().getName()).isEqualTo(place1.getName());
    }

    @Test
    @DisplayName("좋아요 및 상태 업데이트 테스트")
    public void test2() {
        // given
        LikedPlace likedPlace = new LikedPlace(user1, place1);
        entityManager.persist(likedPlace);

        // 초기 좋아요 상태는 false로 설정
        assertThat(likedPlace.isLiked()).isFalse();

        // when: 좋아요 상태를 true로 업데이트
        likedPlace.updateLike(true);
        likedPlaceRepository.save(likedPlace);

        // EntityManager flush 및 clear 후 다시 조회
        entityManager.flush();
        entityManager.clear();

        // then
        LikedPlace updatedLikedPlace = likedPlaceRepository.findByUserIdAndPlaceId(
            user1.getId(), place1.getId()
        ).orElseThrow(() -> InplaceException.of(LikedPlaceErrorCode.NOT_FOUND));

        assertThat(updatedLikedPlace.isLiked()).isTrue();
    }
}
