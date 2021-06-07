package main.link;

public enum OrderType {
    DESC("desc"),
    ASC("asc");
    private final String code;

    OrderType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
