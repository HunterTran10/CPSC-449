package com.example.webbackend.controller;

import com.example.webbackend.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {

    private List<Book> books = new ArrayList<>();

    private Long nextId = 1L;

    public BookController() {
        // Add 15 books with varied data for testing
        books.add(new Book(nextId++, "Spring Boot in Action", "Craig Walls", 39.99));
        books.add(new Book(nextId++, "Effective Java", "Joshua Bloch", 45.00));
        books.add(new Book(nextId++, "Clean Code", "Robert Martin", 42.50));
        books.add(new Book(nextId++, "Java Concurrency in Practice", "Brian Goetz", 49.99));
        books.add(new Book(nextId++, "Design Patterns", "Gang of Four", 54.99));
        books.add(new Book(nextId++, "Head First Java", "Kathy Sierra", 35.00));
        books.add(new Book(nextId++, "Spring in Action", "Craig Walls", 44.99));
        books.add(new Book(nextId++, "Clean Architecture", "Robert Martin", 39.99));
        books.add(new Book(nextId++, "Refactoring", "Martin Fowler", 47.50));
        books.add(new Book(nextId++, "The Pragmatic Programmer", "Andrew Hunt", 41.99));
        books.add(new Book(nextId++, "You Don't Know JS", "Kyle Simpson", 29.99));
        books.add(new Book(nextId++, "JavaScript: The Good Parts", "Douglas Crockford", 32.50));
        books.add(new Book(nextId++, "Eloquent JavaScript", "Marijn Haverbeke", 27.99));
        books.add(new Book(nextId++, "Python Crash Course", "Eric Matthes", 38.00));
        books.add(new Book(nextId++, "Automate the Boring Stuff", "Al Sweigart", 33.50));
    }

    // get all books - /api/books
    @GetMapping("/books")
    public List<Book> getBooks() {
        return books;
    }

    // get books by id
    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Long id) {
        return books.stream().filter(book -> book.getId().equals(id))
                .findFirst().orElse(null);
    }

    // create a new book
    @PostMapping("/books")
    public List<Book> createBook(@RequestBody Book book) {
        books.add(book);
        return books;
    }

    // search by title
    @GetMapping("/books/search")
    public List<Book> searchByTitle (
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        if (title.isEmpty()) {
            return books;
        }
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // price range
    @GetMapping("/books/price-range")
    public List<Book> getBooksByPrice(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return books.stream()
                .filter(book -> {
                    boolean min = minPrice == null || book.getPrice() >= minPrice;
                    boolean max = maxPrice == null || book.getPrice() <= maxPrice;

                    return min && max;
                }).collect(Collectors.toList());
    }

    // sort
    @GetMapping("/books/sorted")
    public List<Book> getSortedBooks(
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        Comparator<Book> comparator;

        switch(sortBy.toLowerCase()) {
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            case "price":
                comparator = Comparator.comparing(Book::getPrice);
                break;
            default:
                comparator = Comparator.comparing(Book::getTitle);
                break;
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return books.stream().sorted(comparator)
                .collect(Collectors.toList());
    }

    // PUT endpoint (update book)
    @PutMapping("/books/{id}")
    public List<Book> fullUpdateBook(@PathVariable Long id, @RequestBody Book newBook) {
        Book book1 = books.stream().filter(book -> book.getId().equals(id))
                .findFirst().orElse(null);

        if (book1 != null) {
            book1.setTitle(newBook.getTitle());
            book1.setAuthor(newBook.getAuthor());
            book1.setPrice(newBook.getPrice());
        }

        return books;
    }

    // PATCH endpoint (partial update)
    @PatchMapping("/books/{id}")
    public List<Book> partialUpdateBook(@PathVariable Long id, @RequestBody Book newBook) {
        Book book1 = books.stream().filter(book -> book.getId().equals(id))
                .findFirst().orElse(null);

        if (book1 != null) {
            if (newBook.getTitle() != null) {
                book1.setTitle(newBook.getTitle());
            }
            if (newBook.getAuthor() != null) {
                book1.setAuthor(newBook.getAuthor());
            }
            if (newBook.getPrice() != null) {
                book1.setPrice(newBook.getPrice());
            }
        }
        return books;
    }

    // DELETE endpoint (remove book)
    @DeleteMapping("/books/{id}")
    public List<Book> deleteBook(@PathVariable Long id) {
        Book book1 = books.stream().filter(book -> book.getId().equals(id))
                .findFirst().orElse(null);

        books.remove(book1);

        return books;
    }

    // GET endpoint with pagination
    @GetMapping("/books/page")
    public List<Book> getBooksWithPagination(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        return books.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Advanced GET endpoint with filtering, sorting, and pagination combined in the valid order
    @GetMapping("/books/advanced")
    public List<Book> getBooksWithFilteringSortingPagination(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String author,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        Comparator<Book> comparator;

        switch(sortBy.toLowerCase()) {
            case "price":
                comparator = Comparator.comparing(Book::getPrice);
                break;
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            default:
                comparator = Comparator.comparing(Book::getTitle);
                break;
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> {
                    boolean min = minPrice == null || book.getPrice() >= minPrice;
                    boolean max = maxPrice == null || book.getPrice() <= maxPrice;

                    return min && max;
                })
                .sorted(comparator)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        }
    }