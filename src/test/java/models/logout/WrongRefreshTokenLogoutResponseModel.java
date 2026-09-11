package models.logout;

public record WrongRefreshTokenLogoutResponseModel
        (String detail,
         String code) {
}
