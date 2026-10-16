package models.book_club;

import java.time.Instant;

public record SuccessfulAddClubReviewResponseModel(
        Integer id,
        Integer club,
        Object user,
        String review,
        Integer assessment,
        Integer readPages,
        Instant created,
        Instant modified) {
}
