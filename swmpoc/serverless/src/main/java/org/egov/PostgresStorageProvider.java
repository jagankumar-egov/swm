package org.egov;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class PostgresStorageProvider implements StorageProvider {

    private static Connection _connection;

    private static AWSSimpleSystemsManagement _ssmclient= AWSSimpleSystemsManagementClientBuilder.defaultClient();

    private static final Logger LOG = Logger.getLogger(String.valueOf(PostgresStorageProvider.class));

    public static final String TEST_TABLE_NAME = "test_table_name";

    @Override
    public Connection getConnection() throws SQLException {
        if(_connection == null || _connection.isClosed())
            _connection = getRemoteConnection();
        return _connection;
    }

    private static Properties getConnectionProperties() {
        Properties properties = new Properties();
        String ssm_userName = System.getenv("RDS_USERNAME_PARAMETER");
        String ssm_password = System.getenv("RDS_PASSWORD_PARAMETER");
        LOG.info("Getting " + ssm_userName + " and " + ssm_password + " from SSM Parameter Store");
        _ssmclient.getParameters(
                new GetParametersRequest()
                    .withNames(ssm_userName, ssm_password)
                .withWithDecryption(true)
        ).getParameters()
                .forEach(parameter -> {
                    if(parameter.getName().equals(ssm_userName))
                        properties.setProperty("user", parameter.getValue());
                    if(parameter.getName().equals(ssm_password))
                        properties.setProperty("password", parameter.getValue());
                });
        LOG.info("Successfully got parameters from ssm");
        return properties;
    }

    private static Connection getRemoteConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String dbName = System.getenv("RDS_DBNAME");
            String endpoint = System.getenv("RDS_ENDPOINT");
            String port = System.getenv("RDS_PORT");
            String jdbcUrl = "jdbc:postgresql://" + endpoint + ":" + port + "/" + dbName;
            LOG.info("Connecting to: " + jdbcUrl);
            Connection con = DriverManager.getConnection(jdbcUrl, getConnectionProperties());
            LOG.info("Successfully created connection");
            //nuke(con);
            ensureTestTableExists(con); // TODO: db schema
            return con;
        }
        catch (ClassNotFoundException e)
        {
            LOG.severe(e.toString());
            throw new RuntimeException(e);
        }
    }

    private static void ensureTestTableExists(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TEST_TABLE_NAME + " (\n" +
                "  id   SERIAL primary key,  \n" +
                "  data JSON   NOT NULL,  \n" +
                "  created_at  TIMESTAMP DEFAULT LOCALTIMESTAMP \n" +
                ");";
        connection.prepareStatement(sql).execute();
        // TODO: use type json rather than text ?
    }

    private static void nuke(Connection connection) throws SQLException {
        String sql = "DROP TABLE " + TEST_TABLE_NAME;
        LOG.info(sql);
        connection.prepareStatement(sql).execute();
    }
}
