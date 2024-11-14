package team7.inplace.admin.presentation;

import team7.inplace.admin.application.dto.BannerInfo;

public class BannerResponse {
    public record Info(
            Long id,
            String imageUrl
    ) {
        public static Info from(BannerInfo.Detail logo) {
            return new Info(logo.id(), logo.imageUrl().replace("https://", "http://"));
        }
    }
}
