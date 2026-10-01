package models.book_club;

public record SuccessfulUpdateBookDataRequestModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink) {
}
