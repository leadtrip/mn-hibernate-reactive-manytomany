package example.micronaut.commands;

import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Serdeable
public class GenreUpdateCommand {

    @NotNull
    private final UUID id;

    @NotBlank
    private final String name;

    public GenreUpdateCommand(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
