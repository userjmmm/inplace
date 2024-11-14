package team7.inplace.influencer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
public class Influencer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 20)
    private String job;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imgUrl;

    public Influencer(String name, String imgUrl, String job) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.job = job;
    }

    public void update(String name, String imgUrl, String job) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.job = job;
    }
}
