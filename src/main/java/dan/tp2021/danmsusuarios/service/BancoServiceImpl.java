package dan.tp2021.danmsusuarios.service;

import org.springframework.stereotype.Service;

import dan.tp2021.danmsusuarios.domain.Cliente;

@Service
public class BancoServiceImpl implements BancoService{
    @Override
    public Boolean verificarRiesgo(Cliente c) {
        return true;
    }
}
