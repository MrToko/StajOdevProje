package com.example.spdemo.mapper;

import com.example.spdemo.model.Book;
import com.example.spdemo.dto.BookDTO;

public class BookMapper {

    public static BookDTO toDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor());
    }

    public static Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        return book;
    }
}
