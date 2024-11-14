package team7.inplace.admin.presentation;

import team7.inplace.admin.application.dto.BannerInfo;

public class BannerResponse {
    public record Info(
            Long id,
            String imageUrl
    ) {
        public static Info from(BannerInfo.Detail banner) {
            return new Info(banner.id(), banner.imageUrl().replace("https://", "http://"));
        }
    }
}
