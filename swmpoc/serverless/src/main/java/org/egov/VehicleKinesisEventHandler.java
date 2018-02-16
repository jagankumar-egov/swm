package org.egov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
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
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readValue(data.array(), JsonNode.class);

            if(jsonNode.has("records")) {
                jsonNode.path("records")
                        .forEach(record -> processRecord(record.path("value")));
            } else {
                processRecord(jsonNode);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to serialize data to JsonNode", e);
            // throw e;
        }
    }

    private static void processRecord(JsonNode record) {
        // TODO: validate JSON fields etc
        try {
            String message = OBJECT_MAPPER.writeValueAsString(record);
            String id = persistToDB(message);
            if (id != null) {
                // TODO: do something with the ID of the inserted message?
            }
            try {
                LOG.info(message);
                SocketIOClient.SendMessage(message); // todo: maybe pass json node here
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error sending message to SocketIO", e);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to serialize data to JsonNode", e);
        }
    }

    private static boolean containsRequiredFields(JsonNode jsonNode, Set<String> requiredFieldNames) {
        if(requiredFieldNames != null) {
            Set<String> missingFields = new HashSet<>(requiredFieldNames);
            jsonNode.fieldNames().forEachRemaining(missingFields::remove);
            if(missingFields.size() > 0) {
                LOG.severe("Missing fields: " + String.join(", ", missingFields));
                return false;
            }
        }
        return true;
    }

    private static String persistToDB(String jsonMessage) {
        try {
            Connection connection = _storageProvider.getConnection();
            String sql = "INSERT INTO " + PostgresStorageProvider.TEST_TABLE_NAME + " (data) values (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(jsonMessage);
            preparedStatement.setObject(1, jsonObject);
            preparedStatement.execute();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                String id = generatedKeys.getString(1);
                LOG.info("id: " + id + " generated for insert");
                return id;
            }
        } catch (SQLException e) {
            LOG.severe(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
