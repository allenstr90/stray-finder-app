package aem.java.strayfinder.persistence.images;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Document(collection = "images")
@Getter
@Setter
@NoArgsConstructor
public class ImageRef implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    private String id;

    private String name;

    private String path;
    private String hash;
    private String mimeType;
    private Binary image;
    private Long strayId;
    @Lob
    @Basic(fetch = FetchType.EAGER)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageRef imageRef)) return false;
        return Objects.equals(id, imageRef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
