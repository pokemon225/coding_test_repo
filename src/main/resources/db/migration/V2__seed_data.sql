INSERT INTO colors (name, sort_order) VALUES
    ('Red', 1),
    ('Orange', 2),
    ('Yellow', 3),
    ('Green', 4),
    ('Blue', 5),
    ('Indigo', 6),
    ('Violet', 7);

INSERT INTO votes (city, color_id, votes)
SELECT s.city, c.id, s.votes
FROM (
    SELECT 'Anchorage' AS city, 'Blue'   AS color, 10000  AS votes UNION ALL
    SELECT 'Anchorage',         'Yellow',          15000           UNION ALL
    SELECT 'Brooklyn',          'Red',             100000          UNION ALL
    SELECT 'Brooklyn',          'Blue',            250000          UNION ALL
    SELECT 'Detroit',           'Red',             160000          UNION ALL
    SELECT 'Selma',             'Yellow',          15000           UNION ALL
    SELECT 'Selma',             'Violet',          5000
) s
JOIN colors c ON c.name = s.color;
