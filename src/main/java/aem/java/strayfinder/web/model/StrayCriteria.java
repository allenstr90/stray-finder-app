package aem.java.strayfinder.web.model;

import aem.java.strayfinder.service.filter.StringFilter;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class StrayCriteria implements Serializable {
    @Serial
    private static final long serialVersionUID = 1293735003891894754L;

    private StringFilter description;
    private StringFilter type;
    private List<String> tags;
}
