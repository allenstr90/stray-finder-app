package aem.java.strayfinder.service.filter;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
public class StringFilter extends GenericFilter<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2622009104826330497L;

    private String contains;
    private String notContains;
}
