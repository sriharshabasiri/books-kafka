package com.harsha.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsha.model.Books;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class BookProducer {

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    public void sendBook(Books book) throws JsonProcessingException {
        CompletableFuture<SendResult<Integer, String>> result = kafkaTemplate.sendDefault(book.getBookId(), new ObjectMapper().writeValueAsString(book));
        result.whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Error sending with exception{}", ex.getMessage());
                    } else {
                        log.info("Message sent for key{}, value{}, to partition{}", res.getProducerRecord().key(),
                                res.getProducerRecord().value(), res.getRecordMetadata().partition());
                    }
                }
        );
    }

    public SendResult<Integer, String> sendBookSync(Books book) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        SendResult<Integer, String> result = kafkaTemplate.sendDefault(book.getBookId(), new ObjectMapper().writeValueAsString(book)).get(1, TimeUnit.SECONDS);
        return result;
    }

    public void sendBookWithTopic(Books book) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<Integer,String> producerRecord = buildProducerRecord(book.getBookId(), new ObjectMapper().writeValueAsString(book),"BookEvent");
        //SendResult<Integer, String> result = kafkaTemplate.send("BookEvent",book.getBookId(), new ObjectMapper().writeValueAsString(book)).get(1, TimeUnit.SECONDS);
        CompletableFuture<SendResult<Integer, String>> result = kafkaTemplate.send(producerRecord);
    }

    private ProducerRecord<Integer,String> buildProducerRecord(Integer bookId, String book, String topic) {
        List<Header> headers = List.of(new RecordHeader("EventSource","scanner".getBytes()),new RecordHeader("EventSource","scanner".getBytes()));
        return new ProducerRecord<>(topic,null,bookId,book,headers);
    }
}
