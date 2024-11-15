package team7.inplace.admin.banner.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;
    private String imgPath;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isFixed;

    private Banner(String imgName, String imgPath, LocalDateTime startDate, LocalDateTime endDate, Boolean isFixed) {
        this.imgName = imgName;
        this.imgPath = imgPath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isFixed = isFixed;
    }

    public static Banner of(String imgName, String imgPath, LocalDateTime startDate, LocalDateTime endDate,
                            Boolean isFixed) {
        return new Banner(imgName, imgPath, startDate, endDate, isFixed);
    }
}
