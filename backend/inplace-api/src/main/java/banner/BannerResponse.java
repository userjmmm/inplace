package banner;

import banner.query.dto.BannerResult;
import banner.query.dto.BannerResult.Admin;
import java.time.LocalDateTime;
import team7.inplace.admin.banner.application.dto.BannerInfo;

public class BannerResponse {

    public record Info(
        Long id,
        String imageUrl,
        Boolean isMain,
        Boolean isMobile,
        Long influencerId
    ) {

        public static Info from(BannerInfo.Detail banner) {
            return new Info(banner.id(), banner.imageUrl(), banner.isMain(), banner.isMobile(),
                banner.influencerId());
        }
    }

    public record Admin(
        Long id,
        String imgPath,
        String imgName,
        Boolean isFixed,
        Boolean isMain,
        Boolean isMobile,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        public static Admin from(BannerResult.Admin banner) {
            return new Admin(
                banner.id(),
                banner.imgPath(),
                banner.imgName(),
                banner.isFixed(),
                banner.isMain(),
                banner.isMobile(),
                banner.startDate(),
                banner.endDate()
            );
        }
    }
}
