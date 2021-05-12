package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.dto.PedidoDTO;

import java.util.List;

public interface PedidoService {
    public List<PedidoDTO> getPedidoByClienteId(Integer id);
}
