package team7.inplace.video.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.place.domain.Place;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Video {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "video_url", nullable = false, columnDefinition = "TEXT")
    private String videoUrl;

    @Embedded
    private View view;

    private Instant publishTime;

    @ManyToOne
    @JoinColumn(name = "influencer_id", nullable = false)
    private Influencer influencer;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private Video(Influencer influencer, Place place, String videoUrl) {
        this.influencer = influencer;
        this.publishTime = Instant.now();
        this.place = place;
        this.videoUrl = videoUrl;
        this.view = new View();
    }

    public Video(Influencer influencer, Place place, String videoUrl, String publishTime) {
        this.influencer = influencer;
        this.publishTime = Instant.parse(publishTime);
        this.place = place;
        this.videoUrl = videoUrl;
        this.view = new View();
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

    public Long getViewCount() {
        return view.getViewCount();
    }

    public Long getViewCountIncrease() {
        return view.getViewCountIncrease();
    }

    public void updateViewCount(Long viewCount) {
        view.updateViewCount(viewCount);
    }

    public void addPlace(Place place) {
        this.place = place;
    }
}
