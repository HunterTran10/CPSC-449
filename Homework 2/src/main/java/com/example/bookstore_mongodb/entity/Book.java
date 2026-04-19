package com.example.bookstore_mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "books")
@Data
public class Book {
    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("isbn")
    @Indexed(unique = true)
    private String isbn;

    @Field("price")
    private BigDecimal price;

    @Field
    private Integer publishedYear;

    @Field
    private Boolean inStock;

    @Field("author")
    private Author author;

    @Field("reviews")
    private List<Review> reviews = new ArrayList<>();

    @Field("tags")
    private List<String> tags = new ArrayList<>();

    @Field("createdAt")
    private Date createdAt;

}
