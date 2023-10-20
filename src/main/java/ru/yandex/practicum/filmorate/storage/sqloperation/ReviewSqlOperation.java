package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum ReviewSqlOperation {
    GET_REVIEW_BY_REVIEW_ID(
            "SELECT * " +
                    "FROM reviews " +
                    "WHERE review_id = ?"),
    UPDATE_REVIEW(
            "UPDATE reviews " +
                    "SET content = ?, " +
                    "is_positive = ? " +
                    "WHERE review_id = ?"),
    DELETE_REVIEW(
            "DELETE FROM reviews " +
                    "WHERE review_id = ?"),
    DELETE_REVIEW_DISLIKES("DELETE FROM review_dislike WHERE review_id = ?"),
    GET_REVIEW_BY_FILM_ID(
            "SELECT * " +
                    "FROM reviews " +
                    "WHERE FILM_ID = ? " +
                    "LIMIT ?"
    ),

    GET_SORT_REVIEW_BY_FILM_ID(
            "SELECT * " +
                    "FROM reviews " +
                    "WHERE film_id = ? " +
                    "ORDER BY useful DESC " +
                    "LIMIT 10;"
    ),

    GET_SORT_ALL_REVIEW(
            "SELECT * " +
                    "FROM reviews " +
                    "ORDER BY useful DESC " +
                    "LIMIT 10;"
    ),

    GET_SORT_REVIEW_WITH_COUNT(
            "SELECT * " +
                    "FROM reviews " +
                    "ORDER BY useful DESC " +
                    "LIMIT ?;"
    ),

    GET_SORT_REVIEW_BY_FILM_ID_WITH_COUNT("SELECT * " +
            "FROM reviews " +
            "WHERE film_id = ? " +
            "ORDER BY useful DESC " +
            "LIMIT ?;"),

    ADD_ONE_USEFUL("UPDATE reviews SET useful = useful + 1 WHERE review_id = ?"),
    ADD_TWO_USEFUL("UPDATE reviews SET useful = useful + 2 WHERE review_id = ?"),
    SUBTRACT_ONE_USEFUL("UPDATE reviews SET useful = useful - 1 WHERE review_id = ?"),
    SUBTRACT_TWO_USEFUL("UPDATE reviews SET useful = useful - 2 WHERE review_id = ?"),

    IS_DISLIKE("SELECT EXISTS(SELECT * FROM review_dislike WHERE review_id = ? AND user_id = ?)"),

    CREATE_DISLIKE(
            "INSERT INTO review_dislike(review_id, user_id) " +
                    "VALUES(?, ?)"),

    DELETE_DISLIKE(
            "DELETE FROM review_dislike " +
                    "WHERE user_id = ? AND review_id = ?"),

    IS_LIKE("SELECT EXISTS(SELECT * FROM review_like WHERE review_id = ? AND user_id = ?)"),

    CREATE_LIKE(
            "INSERT INTO review_like(review_id, user_id) " +
                    "VALUES(?, ?)"),
    DELETE_LIKE(
            "DELETE FROM review_like " +
                    "WHERE user_id = ? AND review_id = ?"),
    ;

    private final String query;

    ReviewSqlOperation(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
