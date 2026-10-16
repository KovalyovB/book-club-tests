package models.localstorage;

public record AuthModel(
        UserLocalStorageModel user,
        String accessToken,
        String refreshToken,
        boolean isAuthenticated
) {
}