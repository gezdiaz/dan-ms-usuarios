package dan.tp2021.danmsusuarios.service.impl;

import dan.tp2021.danmsusuarios.dao.ClienteInMemoryRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
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
    ClienteInMemoryRepository clienteInMemoryRepository;

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
    }

    @Test
    void testCrearClienteRiesgoBancoBien() {
        when(bancoService.verificarRiesgo(any(Cliente.class))).thenReturn(true);
        when(clienteInMemoryRepository.save(any(Cliente.class))).thenReturn(unCliente);
        try {
            Cliente clienteGuardado = clienteService.saveCliente(unCliente);
            assertEquals("12345678",clienteGuardado.getCuit());
        } catch (ClienteException e) {
            fail("Test no cumplido");
        }
    }

    @Test
    void testCrearClienteRiesgoBancoMal(){
        when(bancoService.verificarRiesgo(any(Cliente.class))).thenReturn(false);
        when(clienteInMemoryRepository.save(any(Cliente.class))).thenReturn(unCliente);
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
        when(clienteInMemoryRepository.save(any(Cliente.class))).thenReturn(unCliente);
        when(clienteInMemoryRepository.findById(any(Integer.class))).thenReturn(optionalCliente);

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
        when(clienteInMemoryRepository.save(any(Cliente.class))).thenReturn(unCliente);
        when(clienteInMemoryRepository.findById(any(Integer.class))).thenReturn(optionalCliente);

        try {
            Cliente clienteConFechaBaja = clienteService.darDeBaja(unCliente.getId());
            verify(clienteInMemoryRepository,times(1)).delete(any(Cliente.class));
            //assertNull(clienteConFechaBaja);
        }catch (Exception e){
            fail("Test no cumplido");
        }
    }

}
