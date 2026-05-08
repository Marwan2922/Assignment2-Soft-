package Model;

public enum Category {
    FOOD,
    TRANSPORT,
    ENTERTAINMENT,
    OTHER;

    public String displayName() {
        return switch (this) {
            case FOOD -> "Food";
            case TRANSPORT -> "Transport";
            case ENTERTAINMENT -> "Entertainment";
            case OTHER -> "Other";
        };
    }
}