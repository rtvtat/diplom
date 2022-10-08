package ru.netology.testmode.dao;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class OrderEntity {
    private String id;
    private Date created;
    private String credit_id;
    private String payment_id;
}
