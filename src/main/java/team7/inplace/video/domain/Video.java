package team7.inplace.video.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.place.domain.Place;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Video {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "video_url", nullable = false, columnDefinition = "TEXT")
    private String videoUrl;

    private Long viewCount;
    private Long viewCountIncrease;

    @ManyToOne
    @JoinColumn(name = "influencer_id", nullable = false)
    private Influencer influencer;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private Video(Influencer influencer, Place place, String videoUrl) {
        this.influencer = influencer;
        this.place = place;
        this.videoUrl = videoUrl;
        this.viewCount = -1L;
        this.viewCountIncrease = -1L;
    }

    public static Video from(Influencer influencer, Place place, String videoUrl) {
        return new Video(influencer, place, videoUrl);
    }

    public String getVideoUrl() {
        return String.format("https://www.youtube.com/watch?v=%s", videoUrl);
    }

    public String getVideoUUID() {
        return videoUrl;
    }

    public void updateViewCount(Long viewCount) {
        if (this.viewCount == -1L) {
            this.viewCount = viewCount;
            this.viewCountIncrease = viewCount;
            return;
        }

        this.viewCountIncrease = viewCount - this.viewCount;
        this.viewCount = viewCount;
    }

    public void addPlace(Place place) {
        this.place = place;
    }
}
