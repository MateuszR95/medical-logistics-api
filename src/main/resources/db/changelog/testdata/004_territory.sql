
INSERT INTO territory (id, code, name, territory_type, parent_id, country_id, active)
VALUES (1, 'EUR', 'Europe', 'REGION', NULL, NULL, true),
    (2, 'NORTH_EUR', 'Northern Europe', 'SUBREGION', 1, NULL, true),
    (3, 'CENTRAL_EUR', 'Central Europe', 'SUBREGION', 1, NULL, true),
    (4, 'PL', 'Poland', 'COUNTRY', 2, (SELECT id FROM country WHERE iso2='PL'), true),
    (5, 'BE', 'Belgium', 'COUNTRY', 2, (SELECT id FROM country WHERE iso2='BE'), true),
    (6, 'DK', 'Denmark', 'COUNTRY', 2, (SELECT id FROM country WHERE iso2='DK'), true),
    (7, 'NL', 'Netherlands', 'COUNTRY', 2, (SELECT id FROM country WHERE iso2='NL'), true),
    (8, 'AT', 'Austria', 'COUNTRY', 3, (SELECT id FROM country WHERE iso2='AT'), true),
    (9, 'DE', 'Germany', 'COUNTRY', 3, (SELECT id FROM country WHERE iso2='DE'), true),
    (10, 'FR', 'France', 'COUNTRY', 3, (SELECT id FROM country WHERE iso2='FR'), true),
    (11, 'SOUTH_EUR', 'South Europe', 'SUBREGION', 1, NULL, true),
    (12, 'ES', 'Spain', 'COUNTRY', 11, (SELECT id FROM country WHERE iso2='ES'), true),
    (13, 'IT', 'Italy', 'COUNTRY', 11, (SELECT id FROM country WHERE iso2='IT'), true),
    (14, 'UNITED_KINGDOM', 'United Kingdom', 'SUBREGION', 1, NULL, true),
    (15, 'GB', 'United Kingdom', 'COUNTRY', 14, (SELECT id FROM country WHERE iso2='GB'), true),
    (16, 'NORTH_POLAND', 'North Poland', 'AREA', 4, (SELECT id FROM country WHERE iso2='PL'), true),
    (17, 'SOUTH_POLAND', 'South Poland', 'AREA', 4, (SELECT id FROM country WHERE iso2='PL'), true);


