package com.example.bookstore_mongodb.repository;

import com.example.bookstore_mongodb.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    // find by title
    Optional<Book> findByTitle(String s);

    // find by instock
    List<Book> findByInStock(boolean b);

    // find price less than
    List<Book> findByPriceLessThanEqual(BigDecimal price);

    // find by author name
    List<Book> findByAuthorName(String authorName);

    // find books in a price range
    @Query("{'price' :  {$gt: ?0, $lt :  ?1}}")
    List<Book> getBooksByPriceRange(BigDecimal lower, BigDecimal upper);


}
