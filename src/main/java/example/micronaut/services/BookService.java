package example.micronaut.services;

import example.micronaut.commands.BookCreateCommand;
import example.micronaut.commands.BookUpdateCommand;
import example.micronaut.domain.Book;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Singleton
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransformer bookTransformer;
    private final GenreRepository genreRepository;

    public BookService(BookRepository bookRepository, BookTransformer bookTransformer, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookTransformer = bookTransformer;
        this.genreRepository = genreRepository;
    }

    public Mono<Book> findById(UUID id) {
        return bookRepository
                .findById(id);
    }

    public Flux<Book> list() {
        return bookRepository.findAll();
    }

    public Mono<Book> save( BookCreateCommand bookCreateCommand ) {
        return bookRepository.save(bookTransformer.fromCreateCommand(bookCreateCommand));
    }

    public Mono<Book> update( BookUpdateCommand bookUpdateCommand ) {

        return genreRepository.findAllByIdIn(bookUpdateCommand.getGenres()).collectList()
                .map(genres -> {
                    Book book = bookTransformer.fromUpdateCommandJustId(bookUpdateCommand);
                    book.setGenres(Set.copyOf(genres));
                    return book;
                })
                .flatMap(bookRepository::update);

        //return bookRepository.update(bookTransformer.fromUpdateCommandJustId(bookUpdateCommand));
    }

    public Mono<Book> updateGenres( BookUpdateCommand bookUpdateCommand ) {
        return genreRepository.findAllByIdIn(bookUpdateCommand.getGenres()).collectList()
                .flatMap(genres -> bookRepository.updateGenres(bookUpdateCommand.getId(), Set.copyOf(genres)));
    }

    public Mono<Long> deleteById(UUID id) {
        return bookRepository.deleteById(id);
    }
}
