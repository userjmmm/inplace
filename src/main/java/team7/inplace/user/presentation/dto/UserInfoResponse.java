package team7.inplace.user.presentation.dto;

import team7.inplace.user.application.dto.UserInfo;

public record UserInfoResponse(
    String nickname
) {

    public static UserInfoResponse from(UserInfo userInfo) {
        return new UserInfoResponse(
            userInfo.nickname()
        );
    }
}
