-- Ver todas mis transacciones

SELECT * 
FROM VistaTransaccionesUsuario V
WHERE cliente_id = ?;


-- Ver todas mis transacciones acotadas en un año

SELECT * 
FROM VistaTransaccionesUsuario V
WHERE cliente_id = ?
AND strftime('%Y', fecha) BETWEEN '2022' AND '2024';


-- Ver mis tarjetas asociadas

SELECT T.* 
FROM Tarjeta T
INNER JOIN Cuenta_Tarjeta CT ON CT.nro_tarjeta = T.nro_tarjeta
INNER JOIN Cuenta C ON C.nro_cuenta = CT.nro_cuenta
WHERE nro_cuenta = ?;


-- Ver un resumen de mis gastos (Acotados por año)

SELECT 'Extraccion' AS tipo, IFNULL(SUM(T.monto), 0) AS total
FROM Transaccion T
JOIN Extraccion E ON T.id_transaccion = E.id_transaccion
JOIN Cuenta C ON C.nro_cuenta = E.cuenta_origen
WHERE C.cliente_id = ?  AND strftime('%Y', T.fecha) BETWEEN '2024' AND '2025'
UNION
SELECT 'Transferencia' AS tipo, IFNULL(SUM(T.monto), 0) AS total
FROM Transaccion T
JOIN Transferencia Tr ON T.id_transaccion = Tr.id_transaccion
JOIN Cuenta C ON C.nro_cuenta = Tr.cuenta_origen
WHERE C.cliente_id = ?  AND strftime('%Y', T.fecha) BETWEEN '2024' AND '2025';


-- Ver mis cuentas frecuentes (ultimos 20 personas a las cuales les transferí)

SELECT C.alias, C.cvu, U.nombre, U.apellido
FROM Cuenta C
INNER JOIN Cliente Cli ON Cli.id = C.cliente_id
INNER JOIN Usuario U ON U.id = Cli.id
WHERE C.nro_cuenta IN (
	SELECT cuenta_destino AS nro_cuenta
    FROM Cuenta C2
    INNER JOIN Transferencia T ON T.cuenta_origen = C2.nro_cuenta 
    WHERE C2.cliente_id = ?
)
LIMIT 20;


-- Transferir dinero

BEGIN TRANSACTION;
-- Lógica de negocio (En Java)
-- Hay que verificar que la cuenta a la que transferimos existe
-- Hay que verificar que la cuenta desde la que transferimos tenga saldo

-- Recibimos los datos monto y descripcion
INSERT INTO Transaccion (monto, descripcion, estado) VALUES
(?, ?, 'Pendiente')

-- Se obtiene el ID
SELECT last_insert_rowid() AS id_transaccion;

-- Se obtiene de esta forma el nro_cuenta de destino y origen por alias
SELECT nro_cuenta FROM Cuenta C WHERE C.alias LIKE 'ALGO';

INSERT INTO Transferencia (id_transaccion, cuenta_origen, cuenta_destino)
VALUES (ValorObtenido, ?, ?);

UPDATE Cuenta C SET C.monto  = C.monto - ? WHERE C.cliente_id = ?; -- Le restamos lo que pasó
UPDATE Cuenta C SET C.monto = C.monto + ? WHERE C.cliente_id = ?; -- Le sumamos lo que le pasaron
COMMIT;


-- Depositar dinero

BEGIN TRANSACTION;
-- Se verifica que el dinero ingresó

INSERT INTO Transaccion (monto, descripcion, estado) VALUES
(?, ?, 'Completada')

INSERT INTO Deposito (id_transaccion, cuenta_origen) VALUES
(2, ?),

UPDATE Cuenta C SET C.monto = C.monto + ? WHERE C.cliente_id = ?;
COMMIT;


-- Extraer dinero

BEGIN TRANSACTION;
-- Se verifica que el dinero se extrajo

INSERT INTO Transaccion (monto, descripcion, estado) VALUES
(?, ?, 'Completada')

INSERT INTO Deposito (id_transaccion, cuenta_origen) VALUES
(2, ?),

UPDATE Cuenta C SET C.monto = C.monto - ? WHERE C.cliente_id = ?;
COMMIT;


-- Eliminar alguna tarjeta asociada

BEGIN TRANSACTION;
DELETE FROM Tarjeta T WHERE T.nro_tarjeta IN (
	SELECT * FROM Cuenta_Tarjeta CT
	WHERE CT.id_cliente = ?
);
COMMIT;


-- Eliminar mi cuenta

BEGIN TRANSACTION;
DELETE FROM Cuenta WHERE id_cliente = ?;
DELETE FROM Cliente WHERE id_cliente = ?;
DELETE FROM Usuario WHERE id_cliente = ?
COMMIT; 
