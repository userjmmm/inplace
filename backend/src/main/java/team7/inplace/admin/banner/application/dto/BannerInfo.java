package team7.inplace.admin.banner.application.dto;

import banner.Banner;

public class BannerInfo {

    public record Detail(
        Long id,
        String imageUrl,
        Boolean isMain,
        Boolean isMobile,
        Long influencerId
    ) {

        public static Detail from(Banner banner) {
            return new Detail(
                banner.getId(),
                banner.getImgPath(),
                banner.getIsMain(),
                banner.getIsMobile(),
                banner.getInfluencerId()
            );
        }
    }
}
