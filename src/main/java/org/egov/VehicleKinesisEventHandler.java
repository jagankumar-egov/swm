package org.egov;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehicleKinesisEventHandler implements KinesisEventHandler {

    private static final Logger LOG = Logger.getLogger(String.valueOf(VehicleKinesisEventHandler.class));

    private static StorageProvider _storageProvider;

    public VehicleKinesisEventHandler() {
        _storageProvider = new PostgresStorageProvider();
    }

    public VehicleKinesisEventHandler(StorageProvider storageProvider) {
        _storageProvider = storageProvider;
    }

    @Override
    public void processData(ByteBuffer data) {
        String message = byteBufferToString(data);
        String id = persistToDB(message);
        if(id != null) {
            // do something with the ID of the inserted message?
        }
        try {
            SocketIOClient.SendMessage(message);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error sending message to SocketIO", e);
        }
    }

    private static String persistToDB(String message) {
        try {
            Connection connection = _storageProvider.getConnection();
            String sql = "INSERT INTO " + PostgresStorageProvider.TEST_TABLE_NAME + " (data) values (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, message);
            preparedStatement.execute();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                String id = generatedKeys.getString(1);
                LOG.info("id: " + id + " generated for insert of data: " + message);
                return id;
            }
        } catch (SQLException e) {
            LOG.severe(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
