package ru.netology.testmode.dao;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class CreditEntity {
    private String id;
    private Date created;
    private String bank_id;
    private String status;
}
