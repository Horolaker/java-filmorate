package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum RecommendationSqlOperation {

    GET_FILMS_RECOMMENDATION("SELECT f.film_id, f.film_name, f.description, f.duration, " +
            " f.release_date, r.mpa_id, r.mpa_name " +
            " FROM films AS f " +
            " JOIN rating AS r ON f.mpa_id = r.mpa_id " +
            " WHERE f.film_id in " +
            " ( SELECT DISTINCT film_id " +
            "  FROM FILM_LIKE WHERE user_id in (SELECT user_id FROM FILM_LIKE WHERE film_id in " +
            " ( SELECT film_id from film_like where user_id = ? ) and user_id != ? GROUP BY user_id LIMIT 1 ) ) " +
            " and f.film_id not in " +
            " ( SELECT film_id FROM film_like WHERE user_id = ? ) ");


    private final String query;

    RecommendationSqlOperation(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}
