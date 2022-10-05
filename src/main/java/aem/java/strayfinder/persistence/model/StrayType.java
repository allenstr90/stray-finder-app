package aem.java.strayfinder.persistence.model;

public enum StrayType {
    DOG, CAT, OTHER;

    public static StrayType fromString(String str) {
        if (str == null || str.isEmpty() || str.isBlank())
            return null;
        StrayType[] values = StrayType.values();
        for (StrayType type : values) {
            if (str.trim().toUpperCase().equals(type.name()))
                return type;
        }
        return null;
    }
}
