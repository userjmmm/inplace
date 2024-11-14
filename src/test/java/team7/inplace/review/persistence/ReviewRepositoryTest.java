package team7.inplace.review.persistence;


import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.config.annotation.CustomRepositoryTest;
import team7.inplace.place.domain.Place;
import team7.inplace.review.domain.Review;
import team7.inplace.user.domain.Role;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserType;

@CustomRepositoryTest
@Transactional
class ReviewRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void init() {
        Place place1 = new Place("Place 1",
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

        User user1 = new User("user1", "pass1", "nick1", UserType.KAKAO, Role.USER);
        User user2 = new User("user2", "pass1", "nick1", UserType.KAKAO, Role.USER);
        User user3 = new User("user3", "pass1", "nick1", UserType.KAKAO, Role.USER);
        User user4 = new User("user4", "pass1", "nick1", UserType.KAKAO, Role.USER);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);

        Review review1 = new Review(user1, place1, true, "comment1");
        Review review2 = new Review(user2, place1, true, "comment2");
        Review review3 = new Review(user3, place1, false, "comment3");
        Review review4 = new Review(user4, place1, true, "comment4");

        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.persist(review3);
        entityManager.persist(review4);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("like true false test")
    public void checkLikedDisliked() {
        //given
        //when
        int likes = reviewRepository.countByPlaceIdAndIsLikedTrue(
            1L);

        int dislikes = reviewRepository.countByPlaceIdAndIsLikedFalse(
            1L);

        //then
        assertThat(likes).isEqualTo(3);
        assertThat(dislikes).isEqualTo(1);
    }
}
