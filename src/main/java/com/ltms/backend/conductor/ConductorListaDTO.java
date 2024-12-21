package com.ltms.backend.conductor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConductorListaDTO {
    private Integer idConductor;
    private String fullnameConductor;
}
