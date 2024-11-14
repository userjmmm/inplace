package team7.inplace.admin.presentation;

import team7.inplace.admin.application.dto.LogoInfo;

public class LogoResponse {
    public record Info(
            Long id,
            String imageUrl
    ) {
        public static Info from(LogoInfo.Detail logo) {
            return new Info(logo.id(), logo.imageUrl().replace("https://", "http://"));
        }
    }
}
