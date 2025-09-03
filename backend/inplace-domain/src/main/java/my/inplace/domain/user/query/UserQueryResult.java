package my.inplace.domain.user.query;

public class UserQueryResult {

    public record Simple(
        String nickname,
        String imgUrl,
        String tierName,
        String tierImgUrl,
        String mainBadgeName,
        String mainBadgeImgUrl
    ) {

    }

    public record Badge(
        Long id,
        String name,
        String imgUrl,
        String condition
    ) {

    }
}
