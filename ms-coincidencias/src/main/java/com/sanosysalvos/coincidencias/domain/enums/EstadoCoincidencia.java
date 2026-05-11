package com.sanosysalvos.coincidencias.domain.enums;

public enum EstadoCoincidencia {
    PENDIENTE,  // La coincidencia fue encontrada por el motor pero aún no se revisa
    CONFIRMADA, // Los usuarios confirmaron que es la misma mascota
    RECHAZADA   // Los usuarios indicaron que no es la misma mascota
}
