package ru.netology.testmode.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.dao.CreditEntity;
import ru.netology.testmode.dao.OrderEntity;
import ru.netology.testmode.data.DBHelper;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.page.TravelPageCredit;
import ru.netology.testmode.page.TravelPagePayment;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TravelOfTheDayCreditTest {

    public static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }


    @Test
    @DisplayName("Покупка в кредит подтвержденной картой")
    void paymentCreditTestSuccess() {
        TravelPageCredit page = new TravelPageCredit();
        page.sendData(DataHelper.getAproverCard());
        page.notifySuccess();
        // проверка бд
        OrderEntity order = DBHelper.getOrder();
        CreditEntity paymentById = DBHelper.getCreditById(order.getPayment_id());
        assertNotNull(paymentById);
        assertEquals("APPROVED", paymentById.getStatus());
    }

    @Test
    @DisplayName("Покупка в кредит неподтвержденной картой")
    void paymentDeclinedCreditTestSuccess() {
        TravelPageCredit page = new TravelPageCredit();
        page.sendData(DataHelper.getDeclinedCard());
        page.notifySuccess();
        // проверка бд
        OrderEntity order = DBHelper.getOrder();
        CreditEntity paymentById = DBHelper.getCreditById(order.getPayment_id());
        assertNotNull(paymentById);
        assertEquals("DECLINED", paymentById.getStatus());
    }

    // тесты валидации полей

    // Отправка пустой формы*
    @Test
    @DisplayName("Отправка пустой формы")
    void emptyFormTest() {
        TravelPageCredit page = new TravelPageCredit();
        page.clickContinueButton();
        page.shouldBeVisibleErrorCardNumber();
        page.shouldBeVisibleErrorMonth();
        page.shouldBeVisibleErrorYear();
        page.shouldBeVisibleErrorOwner();
        page.shouldBeVisibleErrorCvc();
    }

    @Test
    @DisplayName("ввод неполного номера карты")
    void incompleteCardNumberTest() {
        TravelPagePayment page = new TravelPagePayment();
        page.clickPayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setNumber("0000 0000 0000 000");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorCardNumber();
    }

    @Test
    @DisplayName("Поле \"Месяц\":* - месяц меньше текущего")
    void monthLessCurrentTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setMonth(DataHelper.getPrevMonth());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorMonth();
    }

    @Test
    @DisplayName("Поле \"Месяц\":* - 00")
    void monthZeroTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setMonth("00");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorMonth();
    }

    @Test
    @DisplayName("Поле \"Месяц\":* - месяц болше 12")
    void monthMore12Test() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setMonth("13");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorMonth();
    }

    @Test
    @DisplayName("Поле \"Год\": - год меньше текущего")
    void yearLessThanCurrentTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setYear(DataHelper.getPrevYear());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorYear();
    }

    @Test
    @DisplayName("Поле \"Год\": - год 00")
    void yearZeroTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setYear("00");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorYear();
    }

    @Test
    @DisplayName("Поле \"Владелец\" спецсимволы")
    void ownerSpecialCharactersTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setOwner(DataHelper.getOwnerWithSpecialCharracter());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorOwner();
    }

    @Test
    @DisplayName("Поле \"Владелец\" пробел")
    void ownerSpaceTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setOwner(" ");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorOwner();
    }

    @Test
    @DisplayName("Поле \"Владелец\" фио с цифрами")
    void ownerWithNumbersTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setOwner(DataHelper.getOwnerWithNumbers());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorOwner();
    }

    @Test
    @DisplayName("Поле \"CVC/CVV\" ввод числа менее 3х знаков")
    void cvcTest() {
        TravelPageCredit page = new TravelPageCredit();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setCvc("11");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorCvc();
    }

}
