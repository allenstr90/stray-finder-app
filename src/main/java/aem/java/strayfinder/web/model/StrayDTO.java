package aem.java.strayfinder.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrayDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1532622802899403649L;

    @Null
    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String type;

    private Set<String> tags;

    private Double longitude;
    private Double latitude;
}
