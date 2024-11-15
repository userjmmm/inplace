package team7.inplace.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.place.domain.Place;
import team7.inplace.user.domain.User;

import java.util.Date;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "place_id"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private boolean isLiked;

    @Column(length = 100)
    private String comment;

    private Date createdDate;

    public Review(User user, Place place, boolean isLiked, String comment) {
        this.user = user;
        this.place = place;
        this.isLiked = isLiked;
        this.comment = comment;
        this.createdDate = new Date();
    }
}
