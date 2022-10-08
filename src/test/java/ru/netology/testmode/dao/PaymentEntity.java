package ru.netology.testmode.dao;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class PaymentEntity {
    private String id;
    private Date created;
    private String amount;
    private String status;
    private String transaction_id;
}
