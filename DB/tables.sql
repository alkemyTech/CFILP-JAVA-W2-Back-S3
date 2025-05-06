
CREATE TABLE Usuario (
  id              INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre          TEXT NOT NULL,
  apellido        TEXT NOT NULL,
  email           TEXT NOT NULL UNIQUE,
  telefono        TEXT,
  fecha_creacion  TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
  contrasena      TEXT NOT NULL
);

CREATE TABLE Cliente (
  id              INTEGER PRIMARY KEY,
  FOREIGN KEY (id) REFERENCES Usuario(id)
);

CREATE TABLE Admin (
  id              INTEGER PRIMARY KEY,
  FOREIGN KEY (id) REFERENCES Usuario(id)
);

CREATE TABLE Cuenta (
  nro_cuenta      TEXT PRIMARY KEY,
  moneda          TEXT NOT NULL,
  monto           REAL NOT NULL DEFAULT 0.0,
  fecha           TEXT NOT NULL,
  alias           TEXT UNIQUE,
  tipo            TEXT NOT NULL,
  cvu             TEXT UNIQUE NOT NULL,
  cliente_id      INTEGER NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE Tarjeta (
  nro_tarjeta       TEXT PRIMARY KEY,
  cod_sec           TEXT NOT NULL,
  fecha_vencimiento TEXT NOT NULL,
  compania          TEXT NOT NULL,
  tipo              TEXT NOT NULL,
  fecha_emision     TEXT NOT NULL,
  particular        TEXT NOT NULL,
  propia            INTEGER NOT NULL DEFAULT 1 -- Indica si es dada por nosotros o por otra compañia como VISA Brubank
);

CREATE TABLE Cuenta_Tarjeta (
  nro_cuenta    TEXT NOT NULL,
  nro_tarjeta   TEXT NOT NULL,
  PRIMARY KEY (nro_cuenta, nro_tarjeta),
  FOREIGN KEY (nro_cuenta) REFERENCES Cuenta(nro_cuenta),
  FOREIGN KEY (nro_tarjeta) REFERENCES Tarjeta(nro_tarjeta)
);

CREATE TABLE Transaccion (
  id_transaccion  INTEGER PRIMARY KEY AUTOINCREMENT,
  monto           REAL NOT NULL,
  descripcion     TEXT,
  fecha           TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado          TEXT NOT NULL
);

CREATE TABLE Deposito (
  id_transaccion  INTEGER PRIMARY KEY,
  cuenta_destino  TEXT NOT NULL,
  FOREIGN KEY (id_transaccion) REFERENCES Transaccion(id_transaccion),
  FOREIGN KEY (cuenta_destino) REFERENCES Cuenta(nro_cuenta)
);

CREATE TABLE Extraccion (
  id_transaccion  INTEGER PRIMARY KEY,
  cuenta_origen   TEXT NOT NULL,
  FOREIGN KEY (id_transaccion) REFERENCES Transaccion(id_transaccion),
  FOREIGN KEY (cuenta_origen) REFERENCES Cuenta(nro_cuenta)
);

CREATE TABLE Transferencia (
  id_transaccion  INTEGER PRIMARY KEY,
  cuenta_origen   TEXT NOT NULL,
  cuenta_destino  TEXT NOT NULL,
  FOREIGN KEY (id_transaccion) REFERENCES Transaccion(id_transaccion),
  FOREIGN KEY (cuenta_origen)  REFERENCES Cuenta(nro_cuenta),
  FOREIGN KEY (cuenta_destino) REFERENCES Cuenta(nro_cuenta)
);


--- Indices

CREATE INDEX idx_transaccion_fecha ON Transaccion(fecha);
CREATE INDEX idx_cuenta_cliente ON Cuenta(cliente_id);
CREATE INDEX idx_cuenta_cvu ON Cuenta(cvu);
CREATE INDEX idx_cuenta_alias ON Cuenta(alias);
CREATE INDEX idx_deposito_destino ON Deposito(cuenta_destino);