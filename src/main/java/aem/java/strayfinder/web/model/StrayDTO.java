package aem.java.strayfinder.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrayDTO implements Serializable {
    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrayDTO strayDTO = (StrayDTO) o;
        return Objects.equals(id, strayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
