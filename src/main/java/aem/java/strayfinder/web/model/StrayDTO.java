package aem.java.strayfinder.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrayDTO implements Serializable {
    @Null
    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String type;
}
