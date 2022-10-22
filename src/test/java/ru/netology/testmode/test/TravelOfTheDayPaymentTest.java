package ru.netology.testmode.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.dao.OrderEntity;
import ru.netology.testmode.dao.PaymentEntity;
import ru.netology.testmode.data.DBHelper;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.page.TravelPagePayment;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class TravelOfTheDayPaymentTest {

    public static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }


    @Test
    @DisplayName("Оплата подтвержденной картой ")
    void paymentCardTestSuccess() {
        TravelPagePayment page = new TravelPagePayment();
        page.sendData(DataHelper.getAproverCard());
        page.notifySuccess();
        // проверка бд
        OrderEntity order = DBHelper.getOrder();
        PaymentEntity paymentById = DBHelper.getPaymentById(order.getPayment_id());
        assertEquals("APPROVED", paymentById.getStatus());
    }

    @Test
    @DisplayName("Оплата картой неподтвержденной картой")
    void paymentDeclinedCardTestSuccess() {
        TravelPagePayment page = new TravelPagePayment();
        page.sendData(DataHelper.getDeclinedCard());
        page.notifySuccess();
        // проверка бд
        OrderEntity order = DBHelper.getOrder();
        PaymentEntity paymentById = DBHelper.getPaymentById(order.getPayment_id());
        assertEquals("DECLINED", paymentById.getStatus());
    }


    @Test
    @DisplayName("Оплата несуществующей картой")
    void paymentNotExistsCardTestSuccess() {
        OrderEntity orderBefore = DBHelper.getOrder();
        TravelPagePayment page = new TravelPagePayment();
        page.sendData(DataHelper.getCardNotExists());
        page.notifyError();
        // проверка бд
        OrderEntity order = DBHelper.getOrder();
        // запись заказа не создана
        String orderId = order == null? null : order.getId();
        String orderIdBefore = orderBefore == null? null : orderBefore.getId();
        assertEquals(orderId, orderIdBefore);
    }

    // тесты валидации полей

    // Отправка пустой формы*
    @Test
    @DisplayName("Отправка пустой формы")
    void emptyFormTest() {
        TravelPagePayment page = new TravelPagePayment();
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
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setNumber("0000 0000 0000 000");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorCardNumber();
    }

    @Test
    @DisplayName("Поле \"Месяц\":* - месяц меньше текущего")
    void monthLessCurrentTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setMonth(DataHelper.getPrevMonth());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorMonth();
    }

    @Test
    @DisplayName("Поле \"Месяц\":* - месяц 00")
    void monthZeroTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setMonth("00");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorMonth();
    }

    @Test
    @DisplayName("Поле \"Месяц\":* - месяц болше 12")
    void monthMore12Test() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setMonth("13");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorMonth();
    }

    @Test
    @DisplayName("Поле \"Год\": - год меньше текущего")
    void yearLessThanCurrentTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setYear(DataHelper.getPrevYear());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorYear();
    }

    @Test
    @DisplayName("Поле \"Год\": - год 00")
    void yearZeroTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setYear("00");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorYear();
    }

    @Test
    @DisplayName("Поле \"Владелец\" спецсимволы")
    void ownerSpecialCharactersTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setOwner(DataHelper.getOwnerWithSpecialCharracter());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorOwner();
    }

    @Test
    @DisplayName("Поле \"Владелец\" пробел")
    void ownerSpaceTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setOwner(" ");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorOwner();
    }

    @Test
    @DisplayName("Поле \"Владелец\" фио с цифрами")
    void ownerWithNumbersTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setOwner(DataHelper.getOwnerWithNumbers());
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorOwner();
    }

    @Test
    @DisplayName("Поле \"CVC/CVV\" ввод числа менее 3х знаков")
    void cvcTest() {
        TravelPagePayment page = new TravelPagePayment();
        DataHelper.CardInfo cardInfo = DataHelper.getAproverCard();
        cardInfo.setCvc("11");
        page.sendData(cardInfo);

        page.clickContinueButton();
        page.shouldBeVisibleErrorCvc();
    }

}
