package aem.java.strayfinder.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.MimeTypeUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImgRefDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6188834396926191023L;

    @Null
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Size(min = 6)
    @Pattern(regexp = "[a-z]{3,}/[a-z]{3,}", message = "Mime type invalid. It should looks like " + MimeTypeUtils.IMAGE_JPEG_VALUE)
    private String mimeType;
}
