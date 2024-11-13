package team7.inplace.token.application.dto;

public class TokenCommand {

    public record ReIssued(
        String accessToken,
        String refreshToken
    ) {

        public static ReIssued of(String reIssuedAccessToken, String reIssuedRefreshToken) {
            return new ReIssued(reIssuedAccessToken, reIssuedRefreshToken);
        }
    }
}
