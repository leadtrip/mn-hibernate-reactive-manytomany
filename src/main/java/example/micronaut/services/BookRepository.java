package example.micronaut.services;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookRepository extends ReactorCrudRepository<Book, UUID> {

    @EntityGraph( attributePaths = {"genres"})
    @NonNull
    @Override
    Mono<Book> findById(@NonNull @NotNull UUID id);

    @EntityGraph( attributePaths = {"genres"})
    @Override
    @NonNull
    Flux<Book> findAll();

    @Transactional
    @EntityGraph( attributePaths = {"genres"})
    default Mono<Book> updateGenres(UUID id, Set<Genre> genres) {
        return findById(id).map( book -> {
            book.setGenres(genres);
            return book;
        });
    }

}
