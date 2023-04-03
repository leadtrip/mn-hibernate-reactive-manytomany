package example.micronaut.services;

import example.micronaut.commands.BookCreateCommand;
import example.micronaut.commands.BookUpdateCommand;
import example.micronaut.domain.Book;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Singleton
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransformer bookTransformer;

    public BookService(BookRepository bookRepository, BookTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookTransformer = bookTransformer;
    }

    public Mono<Book> findById(UUID id) {
        return bookRepository
                .findById(id);
    }

    public Mono<List<Book>> list(Pageable pageable ) {
        return bookRepository.findAll(pageable)
                .map(Page::getContent);
    }

    public Mono<Book> save( BookCreateCommand bookCreateCommand ) {
        return bookRepository.save(bookTransformer.fromCreateCommand(bookCreateCommand));
    }

    public Mono<Book> update( BookUpdateCommand bookUpdateCommand ) {
        return bookRepository.update(bookTransformer.fromUpdateCommandJustId(bookUpdateCommand));
    }

    public Mono<Book> updateGenres( BookUpdateCommand bookUpdateCommand ) {
        return Mono.just(bookTransformer.fromUpdateCommand(bookUpdateCommand))
                .flatMap(book -> bookRepository.updateGenres(book.getId(), book.getGenres()));
    }

    public Mono<Long> deleteById(UUID id) {
        return bookRepository.deleteById(id);
    }
}
