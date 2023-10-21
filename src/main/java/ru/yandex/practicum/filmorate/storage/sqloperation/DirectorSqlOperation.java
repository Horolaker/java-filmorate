package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum DirectorSqlOperation {

    UPDATE_DIRECTOR(
            "UPDATE director " +
                    "SET  name = ? " +
                    "WHERE id = ?;"),

    DELETE_DIRECTOR("DELETE FROM director " +
            "WHERE id=?"),

    GET_ALL_DIRECTORS("SELECT * " +
            "FROM director"),

    GET_DIRECTOR_BY_ID("SELECT * " +
            "FROM director " +
            "WHERE id = ?"),

    GET_DIRECTOR_BY_FILM_ID("SELECT d.id, d.name " +
            "FROM director_films AS df " +
            "LEFT JOIN director AS d ON df.director_id=d.id " +
            "WHERE film_id = ?"),

    ADD_DIRECTOR_TO_FILM("INSERT into director_films (film_id, director_id) values(?, ?);"),

    DELETE_DIRECTOR_FROM_FILM("DELETE FROM director_films " +
            "WHERE film_id = ?");


    private final String query;

    DirectorSqlOperation(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
