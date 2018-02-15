package org.egov;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehicleKinesisEventHandler implements KinesisEventHandler {

    private static final Logger LOG = Logger.getLogger(String.valueOf(VehicleKinesisEventHandler.class));

    private static StorageProvider _storageProvider = new PostgresStorageProvider();

    @Override
    public void handleKinesisEvent(KinesisEvent.KinesisEventRecord kinesisEventRecord) {
        processData(kinesisEventRecord.getKinesis().getData());
    }

    public static void processData(ByteBuffer data) {
        byte[] byteArray = data.array();
        String message = new String(byteArray, Charset.forName("UTF-8"));
        String id = persistToDB(message);
        if(id != null) {
            // do something else?
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
