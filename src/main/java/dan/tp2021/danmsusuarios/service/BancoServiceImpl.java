package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;

public class BancoServiceImpl implements BancoService{
    @Override
    public Boolean verificarRiesgo(Cliente c) {
        return true;
    }
}
