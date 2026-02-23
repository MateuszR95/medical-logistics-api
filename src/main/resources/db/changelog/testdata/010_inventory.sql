INSERT INTO inventory (location_id, item_id, lot_id, qty, allocated_qty)
VALUES

((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-A2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.002'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA127AA'),
    70, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-B1'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.004'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA131AA'),
    55, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-B2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.005'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA133AA'),
    50, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-C1'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.006'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA135AA'),
    45, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-C2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.001'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA125AA'),
    30, 0
),

((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-D1'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.010'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA143AA'),
    35, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-D2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.014'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA151AA'),
    28, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-B2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.024'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA169AA'),
    14, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-C1'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.026'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA173AA'),
    10, 0
),

((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-A1'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.035S'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA193AA'),
    25, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-A2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.036S'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA195AA'),
    18, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-C2'),
    (SELECT i.id FROM item i WHERE i.ref_number='131.000S'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA305AA'),
    40, 0
),

((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-D2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.040'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA201AA'),
    4, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-D2'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.042'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA205AA'),
    2, 0
),
(
    (SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='STO-D1'),
    (SELECT i.id FROM item i WHERE i.ref_number='911.046'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA213AA'),
    3, 0
),

((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='ALLOC-01'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.003'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA130AA'),
    6, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='ALLOC-02'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.019'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA162AA'),
    4, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='ALLOC-03'),
    (SELECT i.id FROM item i WHERE i.ref_number='111.000'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA124AA'),
    10, 0
),

((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='QUAR-IN'),
    (SELECT i.id FROM item i WHERE i.ref_number='121.028S'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA345AA'),
    3, 0
),
((SELECT l.id FROM location l JOIN warehouse w ON w.id=l.warehouse_id
     WHERE w.code='EDC-NL' AND l.code='QUAR-RET'),
    (SELECT i.id FROM item i WHERE i.ref_number='121.015'),
    (SELECT lo.id FROM lot lo WHERE lo.lot_number='AAA239AA'),
    1, 0
);
