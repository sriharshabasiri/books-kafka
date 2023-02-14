package com.harsha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.harsha.model.Books;
import com.harsha.model.Library;
import com.harsha.producer.BookProducer;
import com.harsha.service.BookScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1")
public class BooksController {

    @Autowired
    private BookScanService bookScanService;

    @Autowired
    BookProducer bookProducer;

    @PostMapping("/newbook")
    public ResponseEntity<Books> addBook(@RequestBody Books book) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        //bookProducer.sendBook(book);
        //bookProducer.sendBookSync(book);
        bookProducer.sendBookWithTopic(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/updatebook")
    public String updateBook(@RequestBody Books book){
        bookScanService.updateBook(book);
        return "book updated";
    }

}