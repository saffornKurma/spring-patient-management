package dev.pm.analyticservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient",groupId = "analytics-service")
    public void consumeEvent(byte[] event)
    {
        try {
            PatientEvent pEvent = PatientEvent.parseFrom(event);

            log.info("Received the patient event :[PatientId={},PatientName={},PatientEmain={}]",pEvent.getPatientId(),pEvent.getName(),pEvent.getEmail());

        } catch (InvalidProtocolBufferException e) {
           log.error("Error deseializing event {}",e.getMessage());

        }
    }
}
