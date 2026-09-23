package com.colorvotes;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ColorRepository {

    private final JdbcClient jdbc;

    public ColorRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Color> findAll() {
        return jdbc.sql("SELECT id, name FROM colors ORDER BY sort_order, id")
                .query(Color.class)
                .list();
    }

    /**
     * Sums the votes for one color across all cities. A color that exists but has no
     * votes yields 0; an unknown color id yields empty.
     */
    public Optional<ColorVotes> findVotes(long colorId) {
        return jdbc.sql("""
                        SELECT c.id AS color_id, c.name AS color, COALESCE(SUM(v.votes), 0) AS votes
                        FROM colors c
                        LEFT JOIN votes v ON v.color_id = c.id
                        WHERE c.id = :id
                        GROUP BY c.id, c.name
                        """)
                .param("id", colorId)
                .query(ColorVotes.class)
                .optional();
    }
}
