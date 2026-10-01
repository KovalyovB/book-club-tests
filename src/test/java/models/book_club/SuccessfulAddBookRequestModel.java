package models.book_club;

public record SuccessfulAddBookRequestModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink) {
}
