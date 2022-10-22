package ru.netology.testmode.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Visible;
import lombok.Value;
import ru.netology.testmode.data.DataHelper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class TravelPagePayment {
    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyHeader = $(byText("Оплата по карте"));

    private final SelenideElement creditButton = $(byText("Купить в кредит"));
    private final SelenideElement creditHeader = $(byText("Кредит по данным карты"));

    private final SelenideElement notifySuccess = $(".notification_status_ok");
    private final SelenideElement notifyError = $(".notification_status_error");

    private final SelenideElement cardNumberField = $x("//*[text()='Номер карты']/..//*[@class='input__control']");
    private final SelenideElement cardNumberFieldError = $x("//*[text()='Номер карты']/..//*[@class='input__sub']");

    private final SelenideElement monthField = $x("//*[text()='Месяц']/..//*[@class='input__control']");
    private final SelenideElement monthFieldError = $x("//*[text()='Месяц']/..//*[@class='input__sub']");

    private final SelenideElement yearField = $x("//*[text()='Год']/..//*[@class='input__control']");
    private final SelenideElement yearFieldError = $x("//*[text()='Год']/..//*[@class='input__sub']");

    private final SelenideElement ownerField = $x("//*[text()='Владелец']/..//*[@class='input__control']");
    private final SelenideElement ownerFieldError = $x("//*[text()='Владелец']/..//*[@class='input__sub']");

    private final SelenideElement cvcField = $x("//*[text()='CVC/CVV']/..//*[@class='input__control']");
    private final SelenideElement cvcFieldError = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");

    private final SelenideElement continueButton = $x("//*[text()='Продолжить']/../../../button");

    public void clickPayment() {
        buyButton.click();
        buyHeader.shouldBe(Condition.visible);
    }

    public void clickCredit() {
        creditButton.click();
        creditHeader.shouldBe(Condition.visible);
    }

    public void sendData(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void notifySuccess() {
        notifySuccess.shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    public void notifyError() {
        notifyError.shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    public void shouldBeVisibleErrorCardNumber() {
        cardNumberFieldError.shouldBe(Condition.visible);
    }

    public void shouldBeVisibleErrorMonth() {
        monthFieldError.shouldBe(Condition.visible);
    }

    public void shouldBeVisibleErrorYear() {
        yearFieldError.shouldBe(Condition.visible);
    }

    public void shouldBeVisibleErrorOwner() {
        ownerFieldError.shouldBe(Condition.visible);
    }

    public void shouldBeVisibleErrorCvc() {
        cvcFieldError.shouldBe(Condition.visible);
    }

    @Value
    public static class Card {
        String uuid;
        String number;
        Integer sum;
        SelenideElement button;
    }
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement h2 = $("h2[data-test-id='dashboard']");
    private List<SelenideElement> listElement = $$("li.list__item");


    public TravelPagePayment() {
        clickPayment();
//        h2.shouldBe(Condition.visible, Duration.ofSeconds(5));
    }

    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        for (SelenideElement item : listElement) {
            Card card = extractCardData(item);
            cards.add(card);
        }
        return cards;
    }


    private Card extractCardData(SelenideElement item) {
        String text = item.getText();
        String number = extractNumber(text);
        Integer sum = extractSum(text);
        SelenideElement div = item.$("div[data-test-id]");
        SelenideElement button = item.$("button[data-test-id='action-deposit']");
        String uuid = div.getAttribute("data-test-id");
        return new Card(uuid, number, sum, button);
    }

    private String extractNumber(String text) {
        return text.substring(0, text.indexOf(","));
    }

    private Integer extractSum(String text) {
        String balance = text.substring(text.indexOf(balanceStart)+balanceStart.length(), text.indexOf(balanceFinish));
        return Integer.parseInt(balance);
    }

}
