package com.harsha.service;

import com.harsha.model.Books;
import org.springframework.stereotype.Service;

@Service
public class BookScanService {

    public String addBook(Books book) {



        return "book added";
    }


    public String updateBook(Books book) {
        //bookScanService.scanBook(book);
        return "book updated";
    }
/*
    @Autowired
    private RestTemplate restTemplate;

    public Books scanBook(Books book){
        restTemplate.postForObject("http://localhost:9292/v1/newbook", book,Books.class);
        return book;
    }
*/
}
