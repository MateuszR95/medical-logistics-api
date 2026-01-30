INSERT INTO users
(id, email, first_name, last_name, position_name, role, territory_id, reports_to_id, active, password_hash, created_at, password_changed_at)
VALUES

(1, 'sgalaxy@byom.de', 'Sebastian', 'Galaxy', 'VP Europe Operations', 'MANAGER', 1, NULL, true, '{noop}test', '2025-01-01T09:00:00', NULL),

(2, 'aklein@byom.de', 'Adam', 'Klein', 'Global Admin', 'ADMIN', 1, 1, true, '{noop}test', '2025-01-01T09:05:00', NULL),
(3, 'bwagner@byom.de', 'Beata', 'Wagner', 'Global Admin', 'ADMIN', 1, 1, true, '{noop}test', '2025-01-01T09:06:00', NULL),

(4, 'nivanov@byom.de', 'Nina', 'Ivanov', 'Regional Director - Northern Europe', 'MANAGER', 2, 1, true, '{noop}test', '2025-01-01T09:10:00', NULL),
(5, 'omayer@byom.de', 'Oskar', 'Mayer', 'Regional Director - Central Europe', 'MANAGER', 3, 1, true, '{noop}test', '2025-01-01T09:11:00', NULL),
(6, 'lrodriguez@byom.de', 'Lucia', 'Rodriguez', 'Regional Director - South Europe', 'MANAGER', 11, 1, true, '{noop}test', '2025-01-01T09:12:00', NULL),
(7, 'hclark@byom.de', 'Henry', 'Clark', 'Regional Director - United Kingdom', 'MANAGER', 14, 1, true, '{noop}test', '2025-01-01T09:13:00', NULL),

(8, 'pkowalski@byom.de', 'Piotr', 'Kowalski', 'Regional Sales Rep - North 1', 'SALES', 2, 4, true, '{noop}test', '2025-01-01T09:20:00', NULL),
(9, 'anowak@byom.de', 'Alicja', 'Nowak', 'Regional Sales Rep - North 2', 'SALES', 2, 4, true, '{noop}test', '2025-01-01T09:21:00', NULL),
(10, 'mlewandowski@byom.de', 'Marek', 'Lewandowski', 'Regional Sales Rep - North 3', 'SALES', 2, 4, true, '{noop}test', '2025-01-01T09:22:00', NULL),

(11, 'tschneider@byom.de', 'Tobias', 'Schneider', 'Regional Sales Rep - Central 1', 'SALES', 3, 5, true, '{noop}test', '2025-01-01T09:23:00', NULL),
(12, 'aschmidt@byom.de', 'Anna', 'Schmidt', 'Regional Sales Rep - Central 2', 'SALES', 3, 5, true, '{noop}test', '2025-01-01T09:24:00', NULL),
(13, 'fweber@byom.de', 'Felix', 'Weber', 'Regional Sales Rep - Central 3', 'SALES', 3, 5, true, '{noop}test', '2025-01-01T09:25:00', NULL),

(14, 'mgarcia@byom.de', 'Marta', 'Garcia', 'Regional Sales Rep - South 1', 'SALES', 11, 6, true, '{noop}test', '2025-01-01T09:26:00', NULL),
(15, 'dlopez@byom.de', 'Diego', 'Lopez', 'Regional Sales Rep - South 2', 'SALES', 11, 6, true, '{noop}test', '2025-01-01T09:27:00', NULL),
(16, 'sfernandez@byom.de', 'Sara', 'Fernandez', 'Regional Sales Rep - South 3', 'SALES', 11, 6, true, '{noop}test', '2025-01-01T09:28:00', NULL),

(17, 'jwilson@byom.de', 'Jack', 'Wilson', 'Regional Sales Rep - UK 1', 'SALES', 14, 7, true, '{noop}test', '2025-01-01T09:29:00', NULL),
(18, 'emorgan@byom.de', 'Emma', 'Morgan', 'Regional Sales Rep - UK 2', 'SALES', 14, 7, true, '{noop}test', '2025-01-01T09:30:00', NULL),
(19, 'hthomas@byom.de', 'Harry', 'Thomas', 'Regional Sales Rep - UK 3', 'SALES', 14, 7, true, '{noop}test', '2025-01-01T09:31:00', NULL),

(20, 'kzielinska@byom.de', 'Katarzyna', 'Zielinska', 'Country Manager - Poland', 'MANAGER', 4, 4, true, '{noop}test', '2025-01-01T09:40:00', NULL),
(21, 'vpeeters@byom.de', 'Victor', 'Peeters', 'Country Manager - Belgium', 'MANAGER', 5, 4, true, '{noop}test', '2025-01-01T09:41:00', NULL),
(22, 'mnielsen@byom.de', 'Mikkel', 'Nielsen', 'Country Manager - Denmark', 'MANAGER', 6, 4, true, '{noop}test', '2025-01-01T09:42:00', NULL),
(23, 'djansen@byom.de', 'Daan', 'Jansen', 'Country Manager - Netherlands', 'MANAGER', 7, 4, true, '{noop}test', '2025-01-01T09:43:00', NULL),

(24, 'lhuber@byom.de', 'Lena', 'Huber', 'Country Manager - Austria', 'MANAGER', 8, 5, true, '{noop}test', '2025-01-01T09:44:00', NULL),
(25, 'mbeck@byom.de', 'Moritz', 'Beck', 'Country Manager - Germany', 'MANAGER', 9, 5, true, '{noop}test', '2025-01-01T09:45:00', NULL),
(26, 'cdupont@byom.de', 'Claire', 'Dupont', 'Country Manager - France', 'MANAGER', 10, 5, true, '{noop}test', '2025-01-01T09:46:00', NULL),

(27, 'jromero@byom.de', 'Javier', 'Romero', 'Country Manager - Spain', 'MANAGER', 12, 6, true, '{noop}test', '2025-01-01T09:47:00', NULL),
(28, 'grossi@byom.de', 'Giulia', 'Rossi', 'Country Manager - Italy', 'MANAGER', 13, 6, true, '{noop}test', '2025-01-01T09:48:00', NULL),

(29, 'scooper@byom.de', 'Sarah', 'Cooper', 'Country Manager - United Kingdom', 'MANAGER', 15, 7, true, '{noop}test', '2025-01-01T09:49:00', NULL),

(30, 'jmalinowski@byom.de', 'Julia', 'Malinowski', 'Customer Service - PL 1', 'COUNTRY_CUSTOMER_SERVICE', 4, 20, true, '{noop}test', '2025-01-01T10:00:00', NULL),
(31, 'pkaczmarek@byom.de', 'Pawel', 'Kaczmarek', 'Customer Service - PL 2', 'COUNTRY_CUSTOMER_SERVICE', 4, 20, true, '{noop}test', '2025-01-01T10:01:00', NULL),

(32, 'sdebruyne@byom.de', 'Sofie', 'DeBruyne', 'Customer Service - BE 1', 'COUNTRY_CUSTOMER_SERVICE', 5, 21, true, '{noop}test', '2025-01-01T10:02:00', NULL),
(33, 'tlambert@byom.de', 'Thomas', 'Lambert', 'Customer Service - BE 2', 'COUNTRY_CUSTOMER_SERVICE', 5, 21, true, '{noop}test', '2025-01-01T10:03:00', NULL),

(34, 'fjensen@byom.de', 'Freja', 'Jensen', 'Customer Service - DK 1', 'COUNTRY_CUSTOMER_SERVICE', 6, 22, true, '{noop}test', '2025-01-01T10:04:00', NULL),
(35, 'ehansen@byom.de', 'Emil', 'Hansen', 'Customer Service - DK 2', 'COUNTRY_CUSTOMER_SERVICE', 6, 22, true, '{noop}test', '2025-01-01T10:05:00', NULL),

(36, 'evandermeer@byom.de', 'Eva', 'VanDerMeer', 'Customer Service - NL 1', 'COUNTRY_CUSTOMER_SERVICE', 7, 23, true, '{noop}test', '2025-01-01T10:06:00', NULL),
(37, 'mvandijk@byom.de', 'Mila', 'VanDijk', 'Customer Service - NL 2', 'COUNTRY_CUSTOMER_SERVICE', 7, 23, true, '{noop}test', '2025-01-01T10:07:00', NULL),

(38, 'pstein@byom.de', 'Paula', 'Stein', 'Customer Service - AT 1', 'COUNTRY_CUSTOMER_SERVICE', 8, 24, true, '{noop}test', '2025-01-01T10:08:00', NULL),
(39, 'khofer@byom.de', 'Klara', 'Hofer', 'Customer Service - AT 2', 'COUNTRY_CUSTOMER_SERVICE', 8, 24, true, '{noop}test', '2025-01-01T10:09:00', NULL),

(40, 'lwagner@byom.de', 'Laura', 'Wagner', 'Customer Service - DE 1', 'COUNTRY_CUSTOMER_SERVICE', 9, 25, true, '{noop}test', '2025-01-01T10:10:00', NULL),
(41, 'fhoffmann@byom.de', 'Finn', 'Hoffmann', 'Customer Service - DE 2', 'COUNTRY_CUSTOMER_SERVICE', 9, 25, true, '{noop}test', '2025-01-01T10:11:00', NULL),

(42, 'mbernard@byom.de', 'Manon', 'Bernard', 'Customer Service - FR 1', 'COUNTRY_CUSTOMER_SERVICE', 10, 26, true, '{noop}test', '2025-01-01T10:12:00', NULL),
(43, 'lmoreau@byom.de', 'Louis', 'Moreau', 'Customer Service - FR 2', 'COUNTRY_CUSTOMER_SERVICE', 10, 26, true, '{noop}test', '2025-01-01T10:13:00', NULL),

(44, 'lmartinez@byom.de', 'Lucia', 'Martinez', 'Customer Service - ES 1', 'COUNTRY_CUSTOMER_SERVICE', 12, 27, true, '{noop}test', '2025-01-01T10:14:00', NULL),
(45, 'psanchez@byom.de', 'Pablo', 'Sanchez', 'Customer Service - ES 2', 'COUNTRY_CUSTOMER_SERVICE', 12, 27, true, '{noop}test', '2025-01-01T10:15:00', NULL),

(46, 'mconti@byom.de', 'Marco', 'Conti', 'Customer Service - IT 1', 'COUNTRY_CUSTOMER_SERVICE', 13, 28, true, '{noop}test', '2025-01-01T10:16:00', NULL),
(47, 'cesposito@byom.de', 'Chiara', 'Esposito', 'Customer Service - IT 2', 'COUNTRY_CUSTOMER_SERVICE', 13, 28, true, '{noop}test', '2025-01-01T10:17:00', NULL),

(48, 'owalker@byom.de', 'Oliver', 'Walker', 'Customer Service - GB 1', 'COUNTRY_CUSTOMER_SERVICE', 15, 29, true, '{noop}test', '2025-01-01T10:18:00', NULL),
(49, 'isummers@byom.de', 'Isla', 'Summers', 'Customer Service - GB 2', 'COUNTRY_CUSTOMER_SERVICE', 15, 29, true, '{noop}test', '2025-01-01T10:19:00', NULL),

(50, 'twojcik@byom.de', 'Tomasz', 'Wojcik', 'Sales Rep - PL 1', 'SALES', 4, 20, true, '{noop}test', '2025-01-01T10:30:00', NULL),
(51, 'amazur@byom.de', 'Alicja', 'Mazur', 'Sales Rep - PL 2', 'SALES', 4, 20, true, '{noop}test', '2025-01-01T10:31:00', NULL),

(52, 'lvermeulen@byom.de', 'Lotte', 'Vermeulen', 'Sales Rep - BE 1', 'SALES', 5, 21, true, '{noop}test', '2025-01-01T10:32:00', NULL),
(53, 'bclercq@byom.de', 'Bram', 'Leclercq', 'Sales Rep - BE 2', 'SALES', 5, 21, true, '{noop}test', '2025-01-01T10:33:00', NULL),

(54, 'pchristensen@byom.de', 'Peter', 'Christensen', 'Sales Rep - DK 1', 'SALES', 6, 22, true, '{noop}test', '2025-01-01T10:34:00', NULL),
(55, 'mthomsen@byom.de', 'Maja', 'Thomsen', 'Sales Rep - DK 2', 'SALES', 6, 22, true, '{noop}test', '2025-01-01T10:35:00', NULL),

(56, 'jdeblois@byom.de', 'Joris', 'DeBlois', 'Sales Rep - NL 1', 'SALES', 7, 23, true, '{noop}test', '2025-01-01T10:36:00', NULL),
(57, 'svisser@byom.de', 'Sanne', 'Visser', 'Sales Rep - NL 2', 'SALES', 7, 23, true, '{noop}test', '2025-01-01T10:37:00', NULL),

(58, 'fgruber@byom.de', 'Florian', 'Gruber', 'Sales Rep - AT 1', 'SALES', 8, 24, true, '{noop}test', '2025-01-01T10:38:00', NULL),
(59, 'aberger@byom.de', 'Alina', 'Berger', 'Sales Rep - AT 2', 'SALES', 8, 24, true, '{noop}test', '2025-01-01T10:39:00', NULL),

(60, 'mklein@byom.de', 'Mia', 'Klein', 'Sales Rep - DE 1', 'SALES', 9, 25, true, '{noop}test', '2025-01-01T10:40:00', NULL),
(61, 'jwolf@byom.de', 'Jonas', 'Wolf', 'Sales Rep - DE 2', 'SALES', 9, 25, true, '{noop}test', '2025-01-01T10:41:00', NULL),

(62, 'croux@byom.de', 'Chloe', 'Roux', 'Sales Rep - FR 1', 'SALES', 10, 26, true, '{noop}test', '2025-01-01T10:42:00', NULL),
(63, 'fleroy@byom.de', 'Fabien', 'Leroy', 'Sales Rep - FR 2', 'SALES', 10, 26, true, '{noop}test', '2025-01-01T10:43:00', NULL),

(64, 'dnavarro@byom.de', 'Diana', 'Navarro', 'Sales Rep - ES 1', 'SALES', 12, 27, true, '{noop}test', '2025-01-01T10:44:00', NULL),
(65, 'atorres@byom.de', 'Adrian', 'Torres', 'Sales Rep - ES 2', 'SALES', 12, 27, true, '{noop}test', '2025-01-01T10:45:00', NULL),

(66, 'lromano@byom.de', 'Luca', 'Romano', 'Sales Rep - IT 1', 'SALES', 13, 28, true, '{noop}test', '2025-01-01T10:46:00', NULL),
(67, 'sgreco@byom.de', 'Sara', 'Greco', 'Sales Rep - IT 2', 'SALES', 13, 28, true, '{noop}test', '2025-01-01T10:47:00', NULL),

(68, 'jking@byom.de', 'Joshua', 'King', 'Sales Rep - GB 1', 'SALES', 15, 29, true, '{noop}test', '2025-01-01T10:48:00', NULL),
(69, 'hscott@byom.de', 'Hannah', 'Scott', 'Sales Rep - GB 2', 'SALES', 15, 29, true, '{noop}test', '2025-01-01T10:49:00', NULL),
(70, 'rvaan@byom.de', 'Robin', 'Vaan', 'System Admin', 'ADMIN', 1, 1, true, '{noop}test', '2025-01-01T09:06:00', NULL),

(71, 'jkooners@byom.de', 'Johan', 'Kooners', 'Customer Service - EDC', 'WAREHOUSE_CUSTOMER_SERVICE', 1, 1, true, '{noop}test', '2024-01-01T10:18:00', NULL),
(72, 'rhuntelaar@byom.de', 'Robin', 'Huntelaar', 'Customer Service - EDC', 'WAREHOUSE_CUSTOMER_SERVICE', 1, 1, true, '{noop}test', '2023-01-01T10:19:00', NULL),
(73, 'loomens@byom.de', 'Lutz', 'Loomens', 'Customer Service - EDC', 'WAREHOUSE_CUSTOMER_SERVICE', 1, 1, true, '{noop}test', '2022-01-01T10:19:00', NULL);