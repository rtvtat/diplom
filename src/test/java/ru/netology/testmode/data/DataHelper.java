package ru.netology.testmode.data;

import com.codeborne.selenide.impl.Randomizer;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static final String aproverCard = "4444 4444 4444 4441";
    private static final String declinedCard = "4444 4444 4444 4442";
    private static final String notExistCard = "0000 0000 0000 0001";
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerRus = new Faker(new Locale("ru"));

    public static CardInfo getAproverCard() {
        return new CardInfo(aproverCard, getCurrentMonth(), getYear(), getOwner(), getCvs());
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo(declinedCard, getCurrentMonth(), getYear(), getOwner(), getCvs());
    }

    public static CardInfo getCardNotExists() {
        return new CardInfo(notExistCard, getCurrentMonth(), getYear(), getOwner(), getCvs());
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getPrevMonth() {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getPrevYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getOwner() {
        return faker.name().fullName();
    }

    public static String getOwnerWithNumbers() {
        return faker.name().fullName()+"1";
    }

    public static String getOwnerRus() {
        return fakerRus.name().fullName()+"ё";
    }

    public static String getOwnerWithSpecialCharracter() {
        char[] charracters = new char[] {
                '~', '!', '@', '$', '%', '^', '*', '(', ')', '_', '+', '?'};
        int i = new Random().nextInt(charracters.length - 1);
        return charracters[i] + faker.name().fullName();
    }

    private static String getCvs() {
        int num = new Random().nextInt(999);
        return String.format("%03d", num);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CardInfo {
        private String number;
        private String month;
        private String year;
        private String owner;
        private String cvc;
    }
}
