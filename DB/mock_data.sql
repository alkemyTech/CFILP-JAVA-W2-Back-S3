INSERT INTO Usuario (id_usuario, nombre, apellido, email, telefono, contrasenia, rol) VALUES
(1, 'Juan', 'Perez', 'juan.perez@email.com', '1122334455', 'contrasena123', 'admin'),
(2, 'Maria', 'Lopez', 'maria.lopez@email.com', '2233445566', 'clavesegura456', 'cliente'),
(3, 'Carlos', 'Gomez', 'carlos.gomez@email.com', '3344556677', 'micontra789', 'cliente'),
(4, 'Ana', 'Rodriguez', 'ana.rodriguez@email.com', '4455667788', 'password1234', 'cliente'),
(5, 'Admin', 'Cliente', 'admin.cliente@bank.com', '9988776655', 'adminclientepass', 'cliente'),
(6, 'Laura', 'Fernandez', 'laura.f@email.com', '5566778899', 'laurasegura', 'admin'),
(7, 'Pedro', 'Martinez', 'pedro.m@email.com', '6677889900', 'pedroclave', 'cliente');


INSERT INTO Cuenta (numero_cuenta, moneda, monto, fecha, alias, tipo, cvu, id_usuario) VALUES
('001-123456', 'ARS', 160000.50, '2023-01-15', 'JUAN.PEREZ.CAJA', 'CA', '0000000100000000000001', 1), -- Original era 'Caja de Ahorro'
('001-123456', 'USD', 260.75, '2023-01-15', 'JUAN.PEREZ.DOLAR', 'CA', '0000000100000000000002', 1), -- Original era 'Caja de Ahorro'
('002-987654', 'ARS', 540000.00, '2022-11-20', 'MARIA.LOPEZ.CC', 'CC', '0000000200000000000001', 2), -- Original era 'Cuenta Corriente'
('003-112233', 'ARS', 1275.00, '2024-03-10', 'CARLOS.GOMEZ.CA', 'CA', '0000000300000000000001', 3), -- Original era 'Caja de Ahorro'
('004-445566', 'USD', 1500.00, '2023-05-01', 'ANA.RODRIGUEZ.DOLAR', 'CA', '0000000400000000000001', 4), -- Original era 'Caja de Ahorro'
('004-445566', 'ARS', 78000.00, '2023-05-01', 'ANA.RODRIGUEZ.CA', 'CA', '0000000400000000000002', 4), -- Original era 'Caja de Ahorro'
('005-000111', 'ARS', 50000.00, '2024-04-25', 'ADMIN.CLIENTE.CA', 'CA', '0000000500000000000001', 5), -- Original era 'Caja de Ahorro'
('006-778899', 'ARS', 300000.00, '2024-01-05', 'LAURA.F.CA', 'CA', '0000000600000000000001', 6), -- Original era 'Caja de Ahorro'
('007-112200', 'USD', 45.00, '2024-02-18', 'PEDRO.M.DOLAR', 'CA', '0000000700000000000001', 7); -- Original era 'Caja de Ahorro'


INSERT INTO Tarjeta (numero_tarjeta, codigo_seguridad, fecha_vencimiento, compania, tipo, fecha_emision, particular, propia) VALUES
('4000111122223333', '123', '2028-12-31', 'VISA', 'Credito', '2022-12-01', 'JUAN PEREZ', TRUE), -- Juan (Propia)
('5000444455556666', '456', '2027-11-30', 'Mastercard', 'Debito', '2021-11-15', 'MARIA LOPEZ', TRUE), -- Maria (Propia)
('4000777788889999', '789', '2029-10-31', 'VISA', 'Debito', '2023-09-20', 'ANA RODRIGUEZ', TRUE), -- Ana (Propia)
('5000111100009999', '012', '2026-09-30', 'Mastercard', 'Credito', '2020-08-10', 'ADMIN CLIENTE', TRUE), -- Admin/Cliente (Propia)
('6000333344445555', '345', '2030-08-31', 'AMEX', 'Credito', '2024-07-01', 'LAURA FERNANDEZ', TRUE), -- Laura (Propia)
('5111222233334444', '901', '2026-06-30', 'Mastercard', 'Credito', '2021-05-15', 'ANA RODRIGUEZ', FALSE), -- Ana (Ajena 1)
('4222333344445555', '234', '2025-05-31', 'VISA', 'Debito', '2020-04-01', 'ANA RODRIGUEZ', FALSE); -- Ana (Ajena 2)


INSERT INTO Cuenta_Tarjeta (numero_cuenta, numero_tarjeta) VALUES
('001-123456', '4000111122223333'), -- Juan ARS CA con su tarjeta VISA Credito (Propia)
('002-987654', '5000444455556666'), -- Maria ARS CC con su tarjeta Mastercard Debito (Propia)
('004-445566', '4000777788889999'), -- Ana ARS CA con su tarjeta VISA Debito (Propia)
('004-445566', '5111222233334444'), -- Ana ARS CA con su tarjeta Mastercard Credito (Ajena 1)
('004-445566', '4222333344445555'), -- Ana ARS CA con su tarjeta VISA Debito (Ajena 2)
('005-000111', '5000111100009999'), -- Admin/Cliente ARS CA con su tarjeta Mastercard Credito (Propia)
('006-778899', '6000333344445555'); -- Laura ARS CA con su tarjeta AMEX Credito (Propia)
-- Carlos Gomez (ID 3) y Pedro Martinez (ID 7) no tienen entradas aquí.


INSERT INTO Transaccion (monto, descripcion, fecha, estado) VALUES
(1000.00, 'Deposito inicial Juan', '2023-01-15', 'Completada'), -- id 1 (Deposito Juan)
(500.00, 'Extraccion ATM Maria', '2023-01-20', 'Completada'),   -- id 2 (Extraccion Maria)
(2500.00, 'Transferencia Juan a Maria', '2023-01-22', 'Completada'), -- id 3 (Transferencia Juan)
(1200.00, 'Deposito nomina Maria', '2023-02-01', 'Completada'), -- id 4 (Deposito Maria)
(300.00, 'Compra online Ana', '2023-02-05', 'Completada'),   -- id 5 (Extraccion Ana via tarjeta)
(5000.00, 'Transferencia recibida Juan', '2023-02-10', 'Completada'), -- id 6 (Deposito Juan)
(150.00, 'Extraccion sucursal Laura', '2023-03-01', 'Completada'), -- id 7 (Extraccion Laura)
(800.00, 'Pago de servicio Maria a Juan', '2023-03-15', 'Completada'),   -- id 8 (Transferencia Maria)
(2000.00, 'Transferencia Juan a Carlos', '2024-04-01', 'Completada'), -- id 9 (Transferencia Juan)
(75.00, 'Deposito en efectivo Carlos', '2024-04-05', 'Completada'),   -- id 10 (Deposito Carlos)
(1500.00, 'Transferencia Admin a Laura', '2024-04-10', 'Pendiente'), -- id 11 (Transferencia Admin/Cliente)
(100.00, 'Extraccion fallida Pedro', '2024-04-12', 'Fallida');    -- id 12 (Extraccion Pedro)


INSERT INTO Deposito (id_transaccion, cuenta_destino) VALUES
(1, '001-123456'), -- Deposito Juan
(4, '002-987654'), -- Deposito Maria
(6, '001-123456'), -- Deposito Juan
(10, '003-112233'); -- Deposito Carlos


INSERT INTO Extraccion (id_transaccion, cuenta_origen) VALUES
(2, '002-987654'), -- Extraccion Maria
(5, '004-445566'), -- Extraccion Ana (asociada a compra con tarjeta)
(7, '006-778899'), -- Extraccion Laura
(12, '007-112200'); -- Extraccion Pedro (fallida)


INSERT INTO Transferencia (id_transaccion, cuenta_origen, cuenta_destino) VALUES
(3, '001-123456', '002-987654'), -- Transferencia Juan a Maria
(8, '002-987654', '001-123456'), -- Transferencia Maria a Juan
(9, '001-123456', '003-112233'), -- Transferencia Juan a Carlos
(11, '005-000111', '006-778899'); -- Transferencia Admin/Cliente a Laura
