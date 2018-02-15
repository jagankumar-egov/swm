package org.egov;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
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

    private final Worker worker;

    // TODO: configuration, testing
    // TODO: KCL uses DynamoDB to manage listening across shards, etc

    public SimpleKinesisListener(String kinesisStreamName) {
        final KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(
                this.getClass().getSimpleName(),
                kinesisStreamName,
                DefaultAWSCredentialsProviderChain.getInstance(),
                "WORKERID"
        );
        final IRecordProcessorFactory recordProcessorFactory = new SimpleRecordProcessorFactory();
        worker = new Worker.Builder()
                .recordProcessorFactory(recordProcessorFactory)
                .config(config)
                .build();
    }

    void run() {
        worker.run();
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("first argument should be KinesisStreamName");
        }
        new SimpleKinesisListener(args[0]).run();
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

            private VehicleKinesisEventHandler _kinesisHandler = new VehicleKinesisEventHandler();

            @Override
            public void initialize(InitializationInput initializationInput) {
                LOG.info("initialize SampleRecordProcessor");
            }

            @Override
            public void processRecords(ProcessRecordsInput processRecordsInput) {
                _kinesisHandler.handleEvent(processRecordsInput);
            }

            @Override
            public void shutdown(ShutdownInput shutdownInput) {
                LOG.info("shutdown SampleRecordProcessor");
            }
        }
    }

}
