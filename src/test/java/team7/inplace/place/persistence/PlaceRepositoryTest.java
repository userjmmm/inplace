package team7.inplace.place.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.place.domain.Category;
import team7.inplace.place.domain.Place;

@SpringBootTest
@Transactional
class PlaceRepositoryTest {

    @MockBean
    private PlaceRepository placeRepository;

//     * 테스트 Place 좌표 (longitude, latitude)
//     * (10.0, 10.0) -> video1 -> 성시경
//     * (10.0, 50.0)
//     * (10.0, 100.0)
//     * (50.0, 50.0) -> video2 -> 아이유
//     *
//     * 테스트 좌표
//     * (10.0, 51.0)
//     *
//     * boundary 좌표
//     * 좌상단: (10.0, 60.0)
//     * 우하단: (50.0, 10.0)

    String topLeftLongitude = "10.0";
    String topLeftLatitude = "60.0";
    String bottomRightLongitude = "50.0";
    String bottomRightLatitude = "10.0";
    String longitude = "10.0";
    String latitude = "51.0";
    Pageable pageable = PageRequest.of(0, 10);
    Place place1, place2, place3, place4;

    @BeforeEach
    public void init() {
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

        place2 = new Place("Place 2",
            "\"wifi\": true, \"pet\": false, \"parking\": false, \"forDisabled\": true, \"nursery\": false, \"smokingRoom\": false}",
            "menuImg.url", "일식",
            "Address 1|Address 2|Address 3",
            "10.0", "50.0",
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

        place3 = new Place("Place 3",
            "\"wifi\": true, \"pet\": false, \"parking\": false, \"forDisabled\": true, \"nursery\": false, \"smokingRoom\": false}",
            "menuImg.url", "카페",
            "Address 1|Address 2|Address 3",
            "10.0", "100.0",
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

        place4 = new Place("Place 4",
            "\"wifi\": true, \"pet\": false, \"parking\": false, \"forDisabled\": true, \"nursery\": false, \"smokingRoom\": false}",
            "menuImg.url", "일식",
            "Address 1|Address 2|Address 3",
            "50.0", "50.0",
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

//        Influencer influencer1 = new Influencer("성시경", "가수", "img.url");
//        Influencer influencer2 = new Influencer("아이유", "가수", "img.rul");
//
//        Video video1 = Video.from(influencer1, place1, "video.url");
//        Video video2 = Video.from(influencer2, place4, "video.url");
    }

    @Test
    @DisplayName("거리 기반 장소 조회")
    public void test1() {
        // given
        List<Place> mockPlaces = Arrays.asList(place2, place4, place1, place3);
        when(placeRepository.findPlacesByDistance(longitude, latitude, pageable))
            .thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));

        // when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistance(
            longitude,
            latitude,
            pageable
        );
        System.out.println(foundPlaces);

        // Then
        assertThat(foundPlaces).hasSize(4);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 2");
        assertThat(foundPlaces.getContent().get(1).getName()).isEqualTo("Place 4");
        assertThat(foundPlaces.getContent().get(2).getName()).isEqualTo("Place 1");
        assertThat(foundPlaces.getContent().get(3).getName()).isEqualTo("Place 3");
    }

    @Test
    @DisplayName("필터링 NULL, boundary[(10, 60), (50, 10)]")
    public void test2() {

//         * Place 3(10.0, 100.0) 제외
        // given
        List<String> categories = null;
        List<String> influencers = null;

        List<Place> mockPlaces = Arrays.asList(place2, place4, place1);
        when(placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            null,
            null,
            pageable
        )).thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));

        // when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        );
        // Then
        assertThat(foundPlaces).hasSize(3);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 2");
        assertThat(foundPlaces.getContent().get(1).getName()).isEqualTo("Place 4");
        assertThat(foundPlaces.getContent().get(2).getName()).isEqualTo("Place 1");

    }

    @Test
    @DisplayName("카테고리(japan, cafe) 필터링")
    public void test3() {
        // given
        List<String> categories = Arrays.asList(Category.CAFE.getName(),
            Category.JAPANESE.getName());
        List<String> influencers = null;

        List<Place> mockPlaces = Arrays.asList(place2, place4, place1);
        when(placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            null,
            pageable
        )).thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));
        // when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        );
        // Then
        assertThat(foundPlaces).hasSize(3);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 2");
        assertThat(foundPlaces.getContent().get(0).getCategory()).isEqualTo(Category.JAPANESE);
        assertThat(foundPlaces.getContent().get(1).getName()).isEqualTo("Place 4");
        assertThat(foundPlaces.getContent().get(1).getCategory()).isEqualTo(Category.JAPANESE);
        assertThat(foundPlaces.getContent().get(2).getName()).isEqualTo("Place 1");
        assertThat(foundPlaces.getContent().get(2).getCategory()).isEqualTo(Category.CAFE);
    }

    @Test
    @DisplayName("카테고리(japan) 필터링")
    public void test4() {
        // given
        List<String> categories = Arrays.asList(Category.JAPANESE.getName());
        List<String> influencers = null;

        List<Place> mockPlaces = Arrays.asList(place2, place4);
        when(placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            null,
            pageable
        )).thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));

        // when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        );
        // Then
        assertThat(foundPlaces).hasSize(2);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 2");
        assertThat(foundPlaces.getContent().get(0).getCategory()).isEqualTo(Category.JAPANESE);
        assertThat(foundPlaces.getContent().get(1).getName()).isEqualTo("Place 4");
        assertThat(foundPlaces.getContent().get(1).getCategory()).isEqualTo(Category.JAPANESE);
    }

    @Test
    @DisplayName("인플루언서(아이유, 성시경) 필터링")
    public void test5() {
        // given
        List<String> categories = null;
        List<String> influencers = List.of("성시경", "아이유");

        List<Place> mockPlaces = Arrays.asList(place4, place1);
        when(placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            null,
            influencers,
            pageable
        )).thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));

        //when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        );

        //then
        assertThat(foundPlaces).hasSize(2);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 4");
        assertThat(foundPlaces.getContent().get(1).getName()).isEqualTo("Place 1");
    }

    @Test
    @DisplayName("인플루언서(성시경) 필터링")
    public void test6() {
        // given
        List<String> categories = null;
        List<String> influencers = List.of("성시경");

        List<Place> mockPlaces = Arrays.asList(place1);
        when(placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            null,
            influencers,
            pageable
        )).thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));

        //when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        );

        //then
        assertThat(foundPlaces).hasSize(1);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 1");
    }

    @Test
    @DisplayName("카테고리(Japanese), 인플루언서(아이유) 필터링")
    public void test7() {
        // given
        List<String> categories = Arrays.asList(Category.JAPANESE.getName());
        List<String> influencers = List.of("아이유");

        List<Place> mockPlaces = Arrays.asList(place4);
        when(placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        )).thenReturn(new PageImpl<>(mockPlaces, pageable, mockPlaces.size()));

        //when
        Page<Place> foundPlaces = placeRepository.findPlacesByDistanceAndFilters(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            longitude,
            latitude,
            categories,
            influencers,
            pageable
        );

        //then
        assertThat(foundPlaces).hasSize(1);
        assertThat(foundPlaces.getContent().get(0).getName()).isEqualTo("Place 4");
    }
}
