package example.micronaut.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Serdeable
@Entity
public class Book {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    public Book() {}

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genres=" + genres +
                '}';
    }
}
