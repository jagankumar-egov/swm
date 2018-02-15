package org.egov;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;

import java.util.logging.Logger;

public class SimpleKinesisListener {

    private static final Logger LOG = Logger.getLogger(String.valueOf(SimpleKinesisListener.class));

    private static KinesisEventHandler _eventHandler = new VehicleKinesisEventHandler();

    private final Worker worker;

    // TODO: configuration, testing

    public SimpleKinesisListener() {
        final KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(
                this.getClass().getSimpleName(),
                "TODO: STREAMNAME",
                InstanceProfileCredentialsProvider.getInstance(),
                "WORKERID"
        );
        final IRecordProcessorFactory recordProcessorFactory = new SimpleRecordProcessorFactory();
        worker = new Worker.Builder()
                .recordProcessorFactory(recordProcessorFactory)
                .config(config)
                .build();
    }

    public void run() {
        worker.run();
    }

    public static void main(String[] args) {
        new SimpleKinesisListener().run();
    }

    public class SimpleRecordProcessorFactory implements IRecordProcessorFactory {

        public SimpleRecordProcessorFactory() {
            super();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public IRecordProcessor createProcessor() {
            return new SampleRecordProcessor();
        }

        private class SampleRecordProcessor implements IRecordProcessor {
            @Override
            public void initialize(InitializationInput initializationInput) {
                LOG.info("initialize SampleRecordProcessor");
            }

            @Override
            public void processRecords(ProcessRecordsInput processRecordsInput) {
                processRecordsInput.getRecords().forEach(record -> VehicleKinesisEventHandler.processData(record.getData()));
            }

            @Override
            public void shutdown(ShutdownInput shutdownInput) {
                LOG.info("shutdown SampleRecordProcessor");
            }
        }
    }

}
