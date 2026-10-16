package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static tests.TestData.ADMIN_LEAVE_CLUB_ERROR_MESSAGE;

public class ReviewClubPage {

    private final SelenideElement searchClubInput = $(".search-input");
    private final SelenideElement clubListBlock = $(".clubs-list");
    private final SelenideElement clubContentBlock = $(".club-content");
    private final SelenideElement leaveClubButton = $(".leave-btn");
    private final SelenideElement leaveClubErrorBlock = $(".error");
    private final SelenideElement reviewsBlock = $(".reviews-section");
    private final SelenideElement reviewContentBlock = $(".review-content");


    @Step("Старт web драйвера и добавление localStorage")
    public ReviewClubPage openPageWithPreparedStorage(String localStorageAuthBody) {
        open("/favicon.svg");
        localStorage().setItem("book_club_auth", localStorageAuthBody);

        return this;
    }

    @Step("Открыть главную страницу со списком клубов")
    public ReviewClubPage openClubList() {
        open("https://book-club.qa.guru/");

        return this;
    }

    @Step("Открыть страницу книжного клуба по id")
    public ReviewClubPage openBookClubByUrl(Integer clubId) {
        open(String.format("https://book-club.qa.guru/clubs/%d", clubId));

        return this;
    }

    @Step("Поиск книжного клуба")
    public ReviewClubPage searchClub(String clubName) {
        searchClubInput.setValue(clubName).pressEnter();

        return this;
    }

    @Step("Переход на страницу книжного клуба")
    public ReviewClubPage navigateToBookClubPage(String clubName) {
        clubListBlock.$(byText(clubName))
                .parent().parent().find(".open-btn").click();

        return this;
    }

    @Step("Удаление отзыва о книжном клубе")
    public ReviewClubPage deleteClubReview(String reviewMessage) {
        reviewContentBlock
                .shouldHave(text(reviewMessage))
                .parent()
                .find(".delete-review-btn")
                .click();
        confirm();

        return this;
    }

    @Step("Выход из книжного клуба")
    public ReviewClubPage leaveBookClub() {
        leaveClubButton.click();
        confirm();

        return this;
    }

    @Step("Проверка отсутствия отзыва после удаления")
    public ReviewClubPage checkReviewDeleted(String reviewMessage) {
        reviewsBlock
                .shouldNotHave(text(reviewMessage));

        return this;
    }

    @Step("Проверка загрузки страницы книжного клуба")
    public ReviewClubPage checkClubContent() {
        clubContentBlock.shouldBe(visible);

        return this;
    }

    @Step("Проверка текста ошибки при попытке выхода администратора из книжного клуба")
    public ReviewClubPage checkLeaveAdminError() {
        leaveClubErrorBlock.shouldHave(text(ADMIN_LEAVE_CLUB_ERROR_MESSAGE));

        return this;
    }

    @Step("Проверка отображения блока отзывов")
    public ReviewClubPage checkReviewsBlockDisplayed() {
        reviewsBlock.shouldBe(visible);

        return this;
    }

    @Step("Проверка контента отзыва")
    public ReviewClubPage checkReviewRating(String reviewMessage, Integer assessment) {
        reviewContentBlock
                .shouldHave(text(reviewMessage))
                .parent()
                .find(".stars")
                .shouldHave(matchText("^★{" + assessment + "}☆*$"));

        return this;
    }
}
