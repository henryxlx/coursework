package org.edunext.coursework.dao.impl;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author xulixin
 */
public class DummyDateSource implements DataSource {

    public static final String BEAN_NAME = "dummyDataSource";

    public static final String NOT_VALID_DATA_SOURCE_MSG = "Not a valid data source!";

    private static DummyDateSource instance = new DummyDateSource();

    private DummyDateSource() {
        // reserved.
    }

    public static final DummyDateSource getInstance() {
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw new SQLException(NOT_VALID_DATA_SOURCE_MSG);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLException(NOT_VALID_DATA_SOURCE_MSG);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException(NOT_VALID_DATA_SOURCE_MSG);
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLException(NOT_VALID_DATA_SOURCE_MSG);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new SQLException(NOT_VALID_DATA_SOURCE_MSG);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(NOT_VALID_DATA_SOURCE_MSG);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}