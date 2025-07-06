package com.example.spdemo;

import com.example.spdemo.model.Book;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.MediaType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootBootstrapLiveTest {

    @LocalServerPort
    private int port;
    private String API_ROOT;

    @BeforeEach
    public void setUp() {
        API_ROOT = "http://localhost:" + port + "/api/books";
        RestAssured.port = port;
    }

    private Book createRandomBook() {
        Book book = new Book();
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private String createBookAsUri(Book book) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().getLong("id");
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = RestAssured.get(API_ROOT);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        Book book = createRandomBook();
        createBookAsUri(book);
        Response response = RestAssured.get(API_ROOT + "/title/" + book.getTitle());
        assertEquals(200, response.getStatusCode());
        assertTrue(response.as(java.util.List.class).size() > 0);
    }

    @Test
    public void whenGetCreatedBookById_thenOK() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.get(location);
        assertEquals(200, response.getStatusCode());
        assertEquals(book.getTitle(), response.jsonPath().getString("title"));
    }

    @Test
    public void whenGetNotExistBookById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void whenCreateNewBook_thenCreated() {
        Book book = createRandomBook();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void whenInvalidBook_thenError() {
        Book book = createRandomBook();
        book.setAuthor(null);  // Zorunlu alanı null yaparak hatalı veri gönderiyoruz
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        book.setId(Long.parseLong(location.split("/api/books/")[1]));
        book.setAuthor("newAuthor");
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .put(location);
        assertEquals(200, response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(200, response.getStatusCode());
        assertEquals("newAuthor", response.jsonPath().getString("author"));
    }

    @Test
    public void whenDeleteCreatedBook_thenOk() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.delete(location);
        assertEquals(200, response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(404, response.getStatusCode());
    }
}
