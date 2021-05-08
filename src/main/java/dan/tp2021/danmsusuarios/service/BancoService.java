package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;

public interface BancoService {
    public Boolean verificarRiesgo(Cliente c);
}
