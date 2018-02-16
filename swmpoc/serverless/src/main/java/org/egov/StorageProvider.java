package org.egov;

import java.sql.Connection;
import java.sql.SQLException;

public interface StorageProvider {
    Connection getConnection() throws SQLException;
}
