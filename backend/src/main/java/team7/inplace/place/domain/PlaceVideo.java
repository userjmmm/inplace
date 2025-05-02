package team7.inplace.place.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "place_videos")
public class PlaceVideo {

    @Id
    private Long id;

    private Long placeId;
    private Long videoId;
}
