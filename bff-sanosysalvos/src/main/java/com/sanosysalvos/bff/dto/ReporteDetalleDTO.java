package com.sanosysalvos.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDetalleDTO {
    private Long idReporte;
    private String fechaRegistro;
    private String fechaIncidente;
    private String tipoReporte;
    private String estadoReporte;
    private Long idMascota;
    private Long idUbicacionReporte;

    private UsuarioDetalle usuario;
    private MascotaDetalle mascota;
    private UbicacionDetalle ubicacion;

    @Data
    @Builder
    public static class UsuarioDetalle {
        private String nombre;
        private String email;
        private String telefono;
    }

    @Data
    @Builder
    public static class MascotaDetalle {
        private Long id;
        private String nombre;
        private String descripcion;
        private String raza;
        private String especie;
        private String tamanio;
        private List<String> fotos;
    }

    @Data
    @Builder
    public static class UbicacionDetalle {
        private Long id;
        private Double latitud;
        private Double longitud;
        private String comuna;
        private String region;
        private String codigoH3;
    }
}
