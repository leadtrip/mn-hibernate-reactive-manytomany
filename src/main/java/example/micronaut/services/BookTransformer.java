package example.micronaut.services;

import example.micronaut.commands.BookCreateCommand;
import example.micronaut.commands.BookUpdateCommand;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import jakarta.inject.Singleton;

import java.util.stream.Collectors;

@Singleton
public class BookTransformer {

    private final GenreRepository genreRepository;

    public BookTransformer(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Book fromUpdateCommand(BookUpdateCommand bookUpdateCommand) {
        Book book = new Book();
        book.setId(bookUpdateCommand.getId());
        book.setName(bookUpdateCommand.getName());
        book.setGenres(bookUpdateCommand.getGenres().stream().map(genreId -> genreRepository.findById(genreId).block()).collect(Collectors.toSet()));
        return book;
    }

    public Book fromUpdateCommandJustId(BookUpdateCommand bookUpdateCommand) {
        Book book = new Book();
        book.setId(bookUpdateCommand.getId());
        book.setName(bookUpdateCommand.getName());
        book.setGenres(bookUpdateCommand.getGenres().stream().map(Genre::new).collect(Collectors.toSet()));
        return book;
    }

    public Book fromCreateCommand(BookCreateCommand bookCreateCommand ) {
        Book book = new Book();
        book.setName(bookCreateCommand.getName());
        book.setGenres(bookCreateCommand.getGenres().stream().map(Genre::new).collect(Collectors.toSet()));
        return book;
    }
}
