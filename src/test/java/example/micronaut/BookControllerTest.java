package example.micronaut;

import example.micronaut.commands.BookCreateCommand;
import example.micronaut.commands.BookUpdateCommand;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import example.micronaut.services.GenreRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    GenreRepository genreRepository;

    @Test
    public void testBookCrudOperations() {

        // add a couple of genres
        // genre 1
        Set<Genre> genres = new HashSet<>();
        HttpRequest<?> request = HttpRequest.POST("/genres", Collections.singletonMap("name", "Programming"));
        HttpResponse<?> response = httpClient.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        UUID genreId = entityId(response, "/genres/");
        request = HttpRequest.GET("/genres/" + genreId);
        Genre genre = httpClient.toBlocking().retrieve(request, Genre.class);
        genres.add(genre);

        // genre 2
        request = HttpRequest.POST("/genres", Collections.singletonMap("name", "Database"));
        response = httpClient.toBlocking().exchange(request);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        genreId = entityId(response, "/genres/");
        request = HttpRequest.GET("/genres/" + genreId);
        genre = httpClient.toBlocking().retrieve(request, Genre.class);
        genres.add(genre);

        // genre 3
        request = HttpRequest.POST("/genres", Collections.singletonMap("name", "JSON"));
        response = httpClient.toBlocking().exchange(request);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        genreId = entityId(response, "/genres/");
        request = HttpRequest.GET("/genres/" + genreId);
        genre = httpClient.toBlocking().retrieve(request, Genre.class);
        genres.add(genre);

        assertEquals(3, genres.size());

        // add a book with both genres
        BookCreateCommand bookCreateCommand = new BookCreateCommand(
                "Head first Java",
                genres.stream().map(Genre::getId).limit(2).collect(Collectors.toSet()));

        request = HttpRequest.POST("/books", bookCreateCommand);
        response = httpClient.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        UUID bookId = entityId(response, "/books/");

        // Check the book and associated genres are correct
        request = HttpRequest.GET("/books/" + bookId);

        Book fetchedBook = httpClient.toBlocking().retrieve(request, Book.class);
        System.out.println(fetchedBook);
        assertEquals( genres.size(), genreRepository.count().block() );
        assertEquals( 2, fetchedBook.getGenres().size());

        // remove a genre from the book
        BookUpdateCommand bookUpdateCommand = new BookUpdateCommand();
        bookUpdateCommand.setId(fetchedBook.getId());
        bookUpdateCommand.setName("Java is great");
        bookUpdateCommand.setGenres(Set.of(genreId));

        request = HttpRequest.PUT("/books", bookUpdateCommand);
        httpClient.toBlocking().exchange(request, Argument.of(Book.class), Argument.of(JsonError.class));

        // Fetch the updated book and check genres updates
        request = HttpRequest.GET("/books/" + bookId);
        fetchedBook = httpClient.toBlocking().retrieve(request, Book.class);
        System.out.println(fetchedBook);

        assertEquals( genres.size(), genreRepository.count().block() );
        assertEquals( 1, fetchedBook.getGenres().size());

        // Update book again with all 3 genres
        bookUpdateCommand = new BookUpdateCommand();
        bookUpdateCommand.setId(fetchedBook.getId());
        bookUpdateCommand.setName("Java is great");
        bookUpdateCommand.setGenres(genres.stream().map(Genre::getId).collect(Collectors.toSet()));

        request = HttpRequest.PUT("/books", bookUpdateCommand);
        httpClient.toBlocking().exchange(request, Argument.of(Book.class), Argument.of(JsonError.class));

        // Fetch the updated book and check genres updates
        request = HttpRequest.GET("/books/" + bookId);
        fetchedBook = httpClient.toBlocking().retrieve(request, Book.class);
        System.out.println(fetchedBook);

        assertEquals( genres.size(), genreRepository.count().block() );
        assertEquals( genres.size(), fetchedBook.getGenres().size());

        // Remove all genres associated with book
        bookUpdateCommand = new BookUpdateCommand();
        bookUpdateCommand.setId(fetchedBook.getId());
        bookUpdateCommand.setName("Java is great");
        //bookUpdateCommand.setGenres(Set.of());        // you can do this or just omit, same result, no genre list = remove all

        request = HttpRequest.PUT("/books", bookUpdateCommand);
        httpClient.toBlocking().exchange(request, Argument.of(Book.class), Argument.of(JsonError.class));

        // Fetch the updated book and check genres removed
        request = HttpRequest.GET("/books/" + bookId);
        fetchedBook = httpClient.toBlocking().retrieve(request, Book.class);
        System.out.println(fetchedBook);

        assertEquals( genres.size(), genreRepository.count().block() );
        assertNull( fetchedBook.getGenres() );

        // Use PATCH to update genres
        bookUpdateCommand = new BookUpdateCommand();
        bookUpdateCommand.setId(fetchedBook.getId());
        bookUpdateCommand.setGenres(genres.stream().map(Genre::getId).collect(Collectors.toSet()));

        request = HttpRequest.PATCH("/books", bookUpdateCommand);
        httpClient.toBlocking().exchange(request, Argument.of(Book.class), Argument.of(JsonError.class));

        // Fetch the updated book and check genres removed
        request = HttpRequest.GET("/books/" + bookId);
        fetchedBook = httpClient.toBlocking().retrieve(request, Book.class);
        System.out.println(fetchedBook);

        assertEquals( genres.size(), genreRepository.count().block() );
        assertEquals( genres.size(), fetchedBook.getGenres().size());
    }

    protected UUID entityId(HttpResponse<?> response, String path) {
        String value = response.header(HttpHeaders.LOCATION);
        if (value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if (index != -1) {
            return UUID.fromString(value.substring(index + path.length()));
        }
        return null;
    }
}
