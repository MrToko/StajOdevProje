package com.example.spdemo.service;

import com.example.spdemo.dto.BookDTO;
import com.example.spdemo.mapper.BookMapper;
import com.example.spdemo.model.Book;
import com.example.spdemo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookDTO> findAll() {
        return ((List<Book>) bookRepository.findAll())
                .stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findByTitle(String title) {
        return bookRepository.findByTitle(title)
                .stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        return BookMapper.toDTO(book);
    }

    public BookDTO save(BookDTO bookDTO) {
        Book book = BookMapper.toEntity(bookDTO);
        return BookMapper.toDTO(bookRepository.save(book));
    }

    public void delete(Long id) {
        bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.deleteById(id);
    }

    public BookDTO update(Long id, BookDTO bookDTO) {
        if (!bookDTO.getId().equals(id)) {
            throw new RuntimeException("ID mismatch");
        }
        bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        Book book = BookMapper.toEntity(bookDTO);
        return BookMapper.toDTO(bookRepository.save(book));
    }
}
