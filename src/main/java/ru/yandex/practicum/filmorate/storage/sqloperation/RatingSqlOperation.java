package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum RatingSqlOperation {

    GET_ALL_RATINGS(
            "SELECT * " +
                    "FROM rating"),
    GET_RATING_BY_RATING_ID(
            "SELECT * " +
                    "FROM rating " +
                    "WHERE mpa_id = ?");

    private final String query;

    RatingSqlOperation(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}