package example.micronaut.controllers;

import example.micronaut.commands.BookCreateCommand;
import example.micronaut.commands.BookUpdateCommand;
import example.micronaut.domain.Book;
import example.micronaut.services.BookService;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Get("/{id}")
    public Mono<Book> show(UUID id) {
        return bookService
                .findById(id);
    }

    @Put
    public Mono<Book> update(@Body BookUpdateCommand command) {
        return bookService.update(command);
    }

    @Patch
    public Mono<Book> updateGenres(@Body BookUpdateCommand command) {
        return bookService.updateGenres(command);
    }

    @Get("/list")
    public Mono<List<Book>> list(@Valid Pageable pageable) {
        return bookService.list(pageable);
    }

    @Post
    public Mono<HttpResponse<Book>> save(@Body BookCreateCommand bookCreateCommand) {
        return bookService.save(bookCreateCommand)
                .map(book -> HttpResponse.created(book)
                        .headers(headers -> headers.location(location(book.getId()))));
    }

    @Delete("/{id}")
    public Mono<HttpResponse<?>> delete(UUID id) {
        return bookService.deleteById(id)
                .map(deleteId -> HttpResponse.noContent());
    }

    protected URI location(UUID id) {
        return URI.create("/books/" + id);
    }

}
