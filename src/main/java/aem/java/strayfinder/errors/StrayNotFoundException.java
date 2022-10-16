package aem.java.strayfinder.errors;

public class StrayNotFoundException extends Exception {
    public StrayNotFoundException() {
        super("The stray doesn't exists");
    }
}
