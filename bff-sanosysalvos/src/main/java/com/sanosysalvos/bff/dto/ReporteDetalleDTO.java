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
    private List<CoincidenciaReporteDetalle> coincidencias;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioDetalle {
        private String nombre;
        private String email;
        private String telefono;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MascotaDetalle {
        private Long id;
        private String nombre;
        private String descripcion;
        private String colorPrimario;
        private String colorSecundario;
        private String raza;
        private String especie;
        private String tamanio;
        private String edadAproximada;
        private List<String> fotos;
        private List<String> caracteristicas;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UbicacionDetalle {
        private Long id;
        private Double latitud;
        private Double longitud;
        private String comuna;
        private String region;
        private String codigoH3;
        private String direccionEspecifica;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoincidenciaReporteDetalle {
        private Long idReporte;
        private Double porcentajeSimilitud;
        private String estado;
        private String nombreMascota;
        private String especie;
        private String raza;
        private String tamanio;
        private String fechaIncidente;
        private String comuna;
        private String region;
        private String direccionEspecifica;
        private List<String> fotos;
    }
}
