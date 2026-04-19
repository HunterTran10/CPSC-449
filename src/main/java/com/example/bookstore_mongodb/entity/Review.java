package com.example.bookstore_mongodb.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Review {
    private String usernam;
    private Integer rating;
    private String comment;
    private Date reviewDate;
}
