package team7.inplace.admin.application.dto;

import team7.inplace.admin.domain.Logo;

public class LogoInfo {
    public record Detail(
            Long id,
            String imageUrl
    ) {
        public static Detail from(Logo logo) {
            return new Detail(logo.getId(), logo.getImgPath());
        }
    }
}
