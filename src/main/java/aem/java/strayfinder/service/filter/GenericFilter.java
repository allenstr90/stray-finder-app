package aem.java.strayfinder.service.filter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class GenericFilter<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5256636281121850678L;

    private T equals;
    private T notEquals;
}
