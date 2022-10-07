package aem.java.strayfinder.service.filter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class StringFilter extends GenericFilter<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2622009104826330497L;

    private String contains;
    private String notContains;
}
