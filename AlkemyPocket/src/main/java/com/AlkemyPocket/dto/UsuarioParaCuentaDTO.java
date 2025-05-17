package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioParaCuentaDTO {

    @Schema(example = "7")
    private Integer id;

    @Schema(example = "Ana")
    private String nombre;

    @Schema(example = "García")
    private String apellido;

    @Schema(example = "ana.garcia@example.com")
    private String email;

    @Schema(example = "+5491155443322")
    private String telefono;

    @Schema(example = "cliente")
    private RolUsuario rol;
}
