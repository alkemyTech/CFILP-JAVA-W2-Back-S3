package com.AlkemyPocket.model;

import jakarta.persistence.Entity;

@Entity
public class Cliente extends Usuario{}

/* Extiende de Usuario, hibernate por detrás agregara la conexión PK y FK*/
