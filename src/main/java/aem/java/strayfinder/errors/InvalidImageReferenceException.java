package aem.java.strayfinder.errors;

public class InvalidImageReferenceException extends IllegalArgumentException {
    public InvalidImageReferenceException() {
        super("Invalid image reference. You can't upload that image.");
    }
}
