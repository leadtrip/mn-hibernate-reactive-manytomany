package example.micronaut.services;

import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Repository
public interface GenreRepository extends ReactorPageableRepository<Genre, UUID> {

    Mono<Genre> save(@NonNull @NotBlank String name);

    @Transactional
    default Mono<Genre> saveWithException(@NonNull @NotBlank String name) {
        return save(name)
                .handle((genre, sink) -> {
                    sink.error(new DataAccessException("test exception"));
                });
    }

    Mono<Long> update(@NonNull @NotNull @Id UUID id, @NonNull @NotBlank String name);

    Flux<Genre> findAllByIdIn(@NonNull Set<UUID> ids);
}
