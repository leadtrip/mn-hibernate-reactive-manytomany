package example.micronaut.commands;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Set;
import java.util.UUID;

@Serdeable
public class BookCreateCommand {

    private String name;

    private Set<UUID> genres;

    public BookCreateCommand(String name, Set<UUID> genres) {
        this.name = name;
        this.genres = genres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UUID> getGenres() {
        return genres;
    }

    public void setGenres(Set<UUID> genres) {
        this.genres = genres;
    }
}
