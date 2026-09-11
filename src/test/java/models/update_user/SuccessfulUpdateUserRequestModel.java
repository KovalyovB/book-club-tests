package models.update_user;

public record SuccessfulUpdateUserRequestModel(
        String username,
        String firstName,
        String lastName,
        String email) {
}
