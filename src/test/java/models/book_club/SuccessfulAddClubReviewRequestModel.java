package models.book_club;

public record SuccessfulAddClubReviewRequestModel(
        Integer club,
        String review,
        Integer assessment,
        Integer readPages) {
}
