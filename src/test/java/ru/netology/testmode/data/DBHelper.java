package ru.netology.testmode.data;

import lombok.SneakyThrows;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.testmode.dao.CreditEntity;
import ru.netology.testmode.dao.OrderEntity;
import ru.netology.testmode.dao.PaymentEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;


public class DBHelper {
    private static String datasourceUrl;
    private static String username;
    private static String password;

    private static final String orderQuery = "select * from order_entity order by created desc limit 1;";
    private static final String paymentQuery = "select * from payment_entity where transaction_id = ?;";
    private static final String creditQuery = "select * from credit_request_entity where bank_id = ?;";

    static {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            InputStream is = new FileInputStream("application.properties");
            config.load(is);
            datasourceUrl = config.getString("datasource.url");
            username = config.getString("datasource.username");
            password = config.getString("datasource.password");
        } catch (ConfigurationException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static OrderEntity getOrder() {
        QueryRunner runner = new QueryRunner();
        try (Connection con = DriverManager.getConnection(datasourceUrl, username, password)) {
            return runner.query(con, orderQuery, new BeanHandler<>(OrderEntity.class));
        }
    }
    @SneakyThrows
    public static PaymentEntity getPaymentById(String id) {
        QueryRunner runner = new QueryRunner();
        try (Connection con = DriverManager.getConnection(datasourceUrl, username, password)) {
            return runner.query(con, paymentQuery, new BeanHandler<>(PaymentEntity.class), id);
        }

    }

    @SneakyThrows
    public static CreditEntity getCreditById(String id) {
        QueryRunner runner = new QueryRunner();
        try (Connection con = DriverManager.getConnection(datasourceUrl, username, password)) {
            return runner.query(con, creditQuery, new BeanHandler<>(CreditEntity.class), id);
        }

    }
}
