package team7.inplace.admin.application.dto;

import team7.inplace.admin.domain.Banner;

public class BannerInfo {
    public record Detail(
            Long id,
            String imageUrl
    ) {
        public static Detail from(Banner banner) {
            return new Detail(banner.getId(), banner.getImgPath());
        }
    }
}
