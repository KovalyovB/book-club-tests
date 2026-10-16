package models.book_club;

import java.time.Instant;
import java.util.List;

public record OpenBookClubPageResponseModel(
        Integer id,
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink,
        Integer owner,
        List<Integer> members,
        List<Object> reviews,
        Instant created,
        Instant modified) {
}
