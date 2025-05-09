
CREATE TABLE Usuario (
  id_usuario      SERIAL PRIMARY KEY,
  nombre          TEXT NOT NULL,
  apellido        TEXT NOT NULL,
  email           TEXT NOT NULL UNIQUE,
  telefono        TEXT,
  fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  contrasenia     TEXT NOT NULL
);

CREATE TABLE Cliente (
  id_cliente      INTEGER PRIMARY KEY,
  FOREIGN KEY (id_cliente) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Admin (
  id_admin        INTEGER PRIMARY KEY,
  FOREIGN KEY (id_admin) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Cuenta (
  numero_cuenta   TEXT PRIMARY KEY,
  moneda          TEXT NOT NULL,
  monto           NUMERIC NOT NULL DEFAULT 0.0,
  fecha           DATE NOT NULL,
  alias           TEXT UNIQUE,
  tipo            TEXT NOT NULL,
  cvu             TEXT UNIQUE NOT NULL,
  cliente_id      INTEGER NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES Cliente(id_cliente)
);

CREATE TABLE Tarjeta (
  numero_tarjeta    TEXT PRIMARY KEY,
  codigo_seguridad  TEXT NOT NULL,
  fecha_vencimiento DATE NOT NULL,
  compania          TEXT NOT NULL,
  tipo              TEXT NOT NULL,
  fecha_emision     DATE NOT NULL,
  particular        TEXT NOT NULL,
  propia            BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Cuenta_Tarjeta (
  numero_cuenta    TEXT NOT NULL,
  numero_tarjeta   TEXT NOT NULL,
  PRIMARY KEY (numero_cuenta, numero_tarjeta),
  FOREIGN KEY (numero_cuenta) REFERENCES Cuenta(numero_cuenta),
  FOREIGN KEY (numero_tarjeta) REFERENCES Tarjeta(numero_tarjeta)
);

CREATE TABLE Transaccion (
  id_transaccion  SERIAL PRIMARY KEY,
  monto           NUMERIC NOT NULL,
  descripcion     TEXT,
  fecha           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado          TEXT NOT NULL
);

CREATE TABLE Deposito (
  id_deposito     INTEGER PRIMARY KEY,
  cuenta_destino  TEXT NOT NULL,
  FOREIGN KEY (id_deposito) REFERENCES Transaccion(id_transaccion),
  FOREIGN KEY (cuenta_destino) REFERENCES Cuenta(numero_cuenta)
);

CREATE TABLE Extraccion (
  id_extraccion   INTEGER PRIMARY KEY,
  cuenta_origen   TEXT NOT NULL,
  FOREIGN KEY (id_extraccion) REFERENCES Transaccion(id_transaccion),
  FOREIGN KEY (cuenta_origen) REFERENCES Cuenta(numero_cuenta)
);

CREATE TABLE Transferencia (
  id_transferencia INTEGER PRIMARY KEY,
  cuenta_origen    TEXT NOT NULL,
  cuenta_destino   TEXT NOT NULL,
  FOREIGN KEY (id_transferencia) REFERENCES Transaccion(id_transaccion),
  FOREIGN KEY (cuenta_origen) REFERENCES Cuenta(numero_cuenta),
  FOREIGN KEY (cuenta_destino) REFERENCES Cuenta(numero_cuenta)
);

-- Índices

CREATE INDEX idx_transaccion_fecha ON Transaccion(fecha);
CREATE INDEX idx_cuenta_cliente ON Cuenta(cliente_id);
CREATE INDEX idx_cuenta_cvu ON Cuenta(cvu);
CREATE INDEX idx_cuenta_alias ON Cuenta(alias);
CREATE INDEX idx_deposito_destino ON Deposito(cuenta_destino);
