package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum FilmSqlOperation {

    CREATE_FILM_GENRE(
            "INSERT INTO film_genre(film_id, genre_id) " +
                    "VALUES(?, ?)"),
    GET_ALL_FILMS(
            "SELECT f.film_id, f.film_name, f.description, f.duration, " +
                    "f.release_date, r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id"),
    GET_FILM_BY_FILM_ID(
            "SELECT f.film_id, f.film_name, f.description, f.duration," +
                    "f.release_date, r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id " +
                    "WHERE film_id = ?"),
    UPDATE_FILM(
            "UPDATE films " +
                    "SET film_name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                    "WHERE film_id = ?"),
    DELETE_FILM(
            "DELETE FROM films " +
                    "WHERE film_id = ?"),
    DELETE_FILM_GENRES_BY_FILM_ID(
            "DELETE FROM film_genre " +
                    "WHERE film_id = ?"),
    DELETE_FILM_GENRES_BY_GENRE_ID(
            "DELETE FROM film_genre " +
                    "WHERE genre_id = ?"),
    GET_USER_LIKES_BY_FILM_ID(
            "SELECT user_id " +
                    "FROM film_like " +
                    "WHERE film_id = ?"),
    CREATE_LIKE(
            "INSERT INTO film_like(film_id, user_id) " +
                    "VALUES(?, ?)"),
    DELETE_LIKE(
            "DELETE FROM film_like " +
                    "WHERE film_id = ? AND user_id = ?"),

    GET_DIRECTOR_FILMS_SORTED_BY_YEAR("SELECT f.*, r.mpa_name FROM FILMS f " +
            "LEFT JOIN rating r ON f.mpa_id = r.mpa_id " +
            "LEFT JOIN director_films df ON f.film_id = df.film_id " +
            "LEFT JOIN director d ON df.director_id = d.id " + " WHERE d.id = ? " +
            "ORDER BY EXTRACT(YEAR FROM CAST(release_date AS date))"),

    GET_DIRECTOR_FILMS_SORTED_BY_LIKES("SELECT f.*, r.mpa_name FROM FILMS f " +
            "LEFT JOIN rating r ON f.mpa_id = r.mpa_id " +
            "LEFT JOIN director_films df ON f.film_id = df.film_id " +
            "LEFT JOIN director d ON df.director_id = d.id " +
            "LEFT JOIN film_like fl ON f.film_id = fl.film_id " +
            "WHERE d.id = ? GROUP BY f.film_id " +
            "ORDER BY COUNT(fl.user_id) DESC"),

    GET_MOST_POPULAR_FILMS(
            "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration,r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id " +
                    "LEFT JOIN FILM_LIKE AS l ON f.film_id = l.film_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(l.user_id) DESC " +
                    "LIMIT ?"),

    GET_COMMON_FILMS(
            "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration,r.mpa_id, r.mpa_name  " +
                    "FROM (SELECT f.* FROM films AS f " +
                    "INNER JOIN film_like AS l ON f.film_id = l.film_id " +
                    "WHERE user_id = ?) f " +
                    "INNER JOIN (SELECT f.* FROM films AS f " +
                    "INNER JOIN film_like AS l ON f.film_id = l.film_id " +
                    "WHERE user_id = ?) f2 ON f.film_id = f2.film_id " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id " +
                    "LEFT JOIN FILM_LIKE AS l ON f.film_id = l.film_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(l.user_id) DESC "),

    GET_POPULAR_FILMS(
            "SELECT distinct * from (SELECT  f.film_id, f.film_name, f.description, f.release_date, f.duration,r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id " +
                    "LEFT JOIN FILM_LIKE AS l ON f.film_id = l.film_id " +
                    "LEFT JOIN FILM_GENRE fg ON f.film_id = fg.film_id " +
                    "WHERE CASE WHEN ? <> '-1' " +
                    "THEN (year(release_date) = ?) " +
                    "ELSE (year(release_date) IS NOT NULL ) " +
                    "END " +
                    "AND " +
                    "CASE when ? <> '-1' " +
                    "THEN (fg.genre_id = ?) " +
                    "ELSE (fg.genre_id IS NOT NULL OR fg.genre_id IS NULL ) " +
                    "END " +
                    "GROUP BY f.film_id,fg.genre_id " +
                    "ORDER BY COUNT(l.user_id) DESC " +
                    "LIMIT ?)"),

    SEARCH_FILM_BY_DIRECTOR("SELECT f.*, r.mpa_name " +
            "FROM films f " +
            "LEFT JOIN rating r ON f.mpa_id = r.mpa_id " +
            "LEFT JOIN director_films df ON f.film_id = df.film_id " +
            "LEFT JOIN director d ON df.director_id = d.id " +
            "WHERE lower(d.name) LIKE lower(?) " +
            "ORDER BY (SELECT COUNT(*) FROM film_like fl WHERE fl.film_id = f.film_id) DESC"),

    SEARCH_FILM_BY_TITLE("SELECT f.*, r.mpa_name " +
            "FROM films f " +
            "LEFT JOIN rating r ON f.mpa_id = r.mpa_id " +
            "WHERE lower(f.film_name) LIKE lower(?) " +
            "ORDER BY (SELECT COUNT(*) FROM film_like fl WHERE fl.film_id = f.film_id) DESC"),

    SEARCH_FILM_BY_DIRECTOR_AND_TITLE("SELECT f.*, r.mpa_name " +
            "FROM films f " +
            "LEFT JOIN rating r ON f.mpa_id = r.mpa_id " +
            "LEFT JOIN director_films df ON f.film_id = df.film_id " +
            "LEFT JOIN director d ON df.director_id = d.id " +
            "WHERE lower(d.name) LIKE lower(?) OR lower(f.film_name) LIKE lower(?) " +
            "ORDER BY (SELECT COUNT(*) FROM film_like fl WHERE fl.film_id = f.film_id) DESC"),

    GET_FILMS_GENRES(
            "SELECT  g.* FROM FILM_GENRE fg " +
                    "INNER JOIN GENRE AS g ON fg.genre_id = g.genre_id " +
                    "WHERE fg.FILM_ID = ?");

    private final String query;

    FilmSqlOperation(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
