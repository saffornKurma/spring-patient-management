package dev.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub stub;

    //localhost:9001/billingService/CreatePatientAccount
    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress,
                                    @Value("${billing.service.grpc.port:9001}")int serverPort) {

        log.info("Connecting to Billing Service GRPC service at{}:{}",serverAddress,serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,
                serverPort).usePlaintext().build();
        stub = BillingServiceGrpc.newBlockingStub(channel);

    }

    public BillingResponse createBillingAccount(String patientId,String name,String email) {
        BillingRequest request = BillingRequest.newBuilder().setName(name).setEmail(email).build();
        BillingResponse response= stub.createBillingAccount(request);
        log.info("Received response from the billing service via GRPC: {}",response);
        return response;
    }
}
