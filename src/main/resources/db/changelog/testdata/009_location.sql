INSERT INTO location (warehouse_id, code, zone, active)
VALUES
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'REC-01', 'RECEIVING_REPACK', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'REC-02', 'RECEIVING_REPACK', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-A1', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-A2', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-B1', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-B2', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-C1', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-C2', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-D1', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'STO-D2', 'STORAGE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'ALLOC-01', 'ALLOCATED', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'ALLOC-02', 'ALLOCATED', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'ALLOC-03', 'ALLOCATED', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'QUAR-IN', 'QUARANTINE', true),
((SELECT id FROM warehouse WHERE code='EDC-NL'), 'QUAR-RET', 'QUARANTINE', true);
