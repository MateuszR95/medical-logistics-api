INSERT INTO warehouse (code, name, address_id, active)
VALUES
    ('US-MAIN','Main US Warehouse (US-MAIN)',
    (SELECT id FROM address WHERE country_code='US' AND city='Memphis' AND line1='1450 Warehouse Way'),
    true),
    ('EDC-NL','European Distribution Centre (EDC)',
    (SELECT id FROM address WHERE country_code='NL' AND city='Rotterdam' AND line1='Distributieweg 1'),
    true);
