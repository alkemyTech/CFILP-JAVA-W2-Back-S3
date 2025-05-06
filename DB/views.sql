CREATE VIEW VistaTransaccionesUsuario AS
SELECT 
  T.id_transaccion,
  T.monto,
  T.descripcion,
  T.fecha,
  T.estado,
  'Ingreso' AS tipo,
  I.cuenta_destino AS cuenta,
  NULL AS cuenta_origen,
  C.cliente_id
FROM Transaccion T
JOIN Ingreso I ON T.id_transaccion = I.id_transaccion
JOIN Cuenta C ON I.cuenta_destino = C.nro_cuenta

UNION

SELECT 
  T.id_transaccion,
  T.monto,
  T.descripcion,
  T.fecha,
  T.estado,
  'Egreso' AS tipo,
  E.cuenta_origen AS cuenta,
  NULL AS cuenta_destino,
  C.cliente_id
FROM Transaccion T
JOIN Egreso E ON T.id_transaccion = E.id_transaccion
JOIN Cuenta C ON E.cuenta_origen = C.nro_cuenta

UNION

SELECT 
  T.id_transaccion,
  T.monto,
  T.descripcion,
  T.fecha,
  T.estado,
  'Transferencia' AS tipo,
  TR.cuenta_origen,
  TR.cuenta_destino,
  C.cliente_id
FROM Transaccion T
JOIN Transferencia TR ON T.id_transaccion = TR.id_transaccion
JOIN Cuenta C ON TR.cuenta_origen = C.nro_cuenta OR TR.cuenta_destino = C.nro_cuenta;