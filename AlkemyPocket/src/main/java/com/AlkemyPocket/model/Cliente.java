package com.AlkemyPocket.model;

import jakarta.persistence.Entity;

@Entity
public class Cliente extends Usuario {}

/* Extiende de Usuario, hibernate por detrás agregara la conexión PK y FK*/
/* IMPORTANTE: En la tabla cliente en postgres, debe llamarse id_usuario, NO id_cliente */
