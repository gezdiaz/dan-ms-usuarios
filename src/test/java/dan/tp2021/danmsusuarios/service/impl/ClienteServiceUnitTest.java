package dan.tp2021.danmsusuarios.service.impl;

import dan.tp2021.danmsusuarios.dao.ClienteRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.dto.PedidoDTO;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNoHabilitadoException;
import dan.tp2021.danmsusuarios.service.BancoService;
import dan.tp2021.danmsusuarios.service.ClienteService;
import dan.tp2021.danmsusuarios.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClienteServiceUnitTest {

    @Autowired
    ClienteService clienteService;

    @MockBean
    BancoService bancoService;

    @MockBean
    ClienteRepository clienteRepository;

    @MockBean
    PedidoService pedidoService;

    Cliente unCliente;

    @BeforeEach
    void setUp(){
        unCliente = new Cliente();
        unCliente.setId(1);
        unCliente.setCuit("12345678");
        unCliente.setMail("mail@mail.com");
        unCliente.setRazonSocial("Nombre");
        //El cliente tiene que tener obras, no se pueden guardar clientes in obras.
        List<Obra> obras = new ArrayList<>();
        obras.add(new Obra());
        unCliente.setObras(obras);
    }

    @Test
    void testCrearClienteRiesgoBancoBien() {
        when(bancoService.verificarRiesgo(any(Cliente.class))).thenReturn(true);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
        try {
            Cliente clienteGuardado = clienteService.saveCliente(unCliente);
            assertEquals("12345678",clienteGuardado.getCuit());
        } catch (Exception e) {
            fail("Test no cumplido");
        }
    }

    @Test
    void testCrearClienteRiesgoBancoMal(){
        when(bancoService.verificarRiesgo(any(Cliente.class))).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
        ClienteNoHabilitadoException exception = assertThrows(ClienteNoHabilitadoException.class, () -> clienteService.saveCliente(unCliente));
        assertEquals("Error. El cliente no cumple con los requisitos de riesgo.",exception.getMessage());
    }

    @Test
    void darDeBajaConPedido(){
        PedidoDTO pedidoDTO = new PedidoDTO();
        List<PedidoDTO> list = new ArrayList<>();
        list.add(pedidoDTO);
        Optional<Cliente> optionalCliente = Optional.of(unCliente);
        when(pedidoService.getPedidoByClienteId(any(Integer.class))).thenReturn(list);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
        when(clienteRepository.findById(any(Integer.class))).thenReturn(optionalCliente);

        try {
            Cliente clienteConFechaBaja = clienteService.darDeBaja(unCliente.getId());
            assertNotNull(clienteConFechaBaja.getFechaBaja());
        }catch (Exception e){
            fail("Test no cumplido");
        }
    }

    @Test
    void darDeBajaSinPedido(){
        //PedidoDTO pedidoDTO = new PedidoDTO();
        List<PedidoDTO> list = new ArrayList<>();
        //list.add(pedidoDTO);
        Optional<Cliente> optionalCliente = Optional.of(unCliente);
        when(pedidoService.getPedidoByClienteId(any(Integer.class))).thenReturn(list);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
        when(clienteRepository.findById(any(Integer.class))).thenReturn(optionalCliente);

        try {
            Cliente clienteConFechaBaja = clienteService.darDeBaja(unCliente.getId());
            verify(clienteRepository,times(1)).deleteById(unCliente.getId());
            //assertNull(clienteConFechaBaja);
        }catch (Exception e){
            fail("Test no cumplido");
        }
    }

}
