package com.kechinvv.ghsearch.repository.link;

public enum SortType {
    STARS("stars"),
    FORKS("forks"),
    HELP_WANTED_ISSUES("help-wanted-issues"),
    BEST_MATCH(""),
    UPDATED("updated");
    private final String code;

    SortType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
