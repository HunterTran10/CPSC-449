package com.example.bookstore_mongodb.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Author {
    @Field
    private String name;
    @Field
    private String nationality;
    @Field
    private Integer birthYear;
}
