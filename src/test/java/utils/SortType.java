package utils;

public enum SortType {
    DATE_OLD_NEW("date_modified-ASC"),
    DATE_NEW_OLD("date_modified-DESC"),
    NAME_A_Z("pd.name-ASC"),
    NAME_Z_A("pd.name-DESC"),
    PRICE_LOW_HIGH("p.price-ASC"),
    PRICE_HIGH_LOW("p.price-DESC"),
    RATING_HIGHEST("rating-DESC"),
    RATING_LOWEST("rating-ASC");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

