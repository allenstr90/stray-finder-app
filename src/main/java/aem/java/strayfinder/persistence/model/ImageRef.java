package aem.java.strayfinder.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ImageRef extends AuditEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    private Long id;

    private String name;

    private String path;
    private String hash;
    private String mimeType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "blob")
    private byte[] bytes;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Stray stray;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageRef imageRef)) return false;
        return Objects.equals(id, imageRef.id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
