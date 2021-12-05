package org.edunext.coursework.kernel.dao;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataSourceConfigTest {

    @Test
    public void testFormatConnectionString() {
        String dbHost = "localhost";
        String dbPort = "3306";
        String url = DataSourceConfig.getMysqlJdbcUrl(dbHost, dbPort);
        String expectedUrl = String.format(DataSourceConfig.JDBC_URL_MYSQL_HOST_FORMATTER +
                DataSourceConfig.JDBC_URL_DATABASE_SETTING_FORMATTER, dbHost, dbPort);
        assertEquals(expectedUrl, url);
        System.out.println(url);
        String dbName = "test";
        url = DataSourceConfig.getMysqlJdbcUrl(dbHost, dbPort, dbName);
        expectedUrl = String.format(DataSourceConfig.JDBC_URL_MYSQL_HOST_FORMATTER + "/%s" +
                DataSourceConfig.JDBC_URL_DATABASE_SETTING_FORMATTER, dbHost, dbPort, dbName);
        assertEquals(expectedUrl, url);
        System.out.println(url);
    }

}