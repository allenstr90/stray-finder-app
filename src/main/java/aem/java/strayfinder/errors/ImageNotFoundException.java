package aem.java.strayfinder.errors;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
        super("Image not found.");
    }
}
