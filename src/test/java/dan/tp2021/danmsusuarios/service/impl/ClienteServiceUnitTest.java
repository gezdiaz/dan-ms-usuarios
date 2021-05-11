package dan.tp2021.danmsusuarios.service.impl;

import dan.tp2021.danmsusuarios.dao.ClienteRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.service.BancoService;
import dan.tp2021.danmsusuarios.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClienteServiceUnitTest {

    @Autowired
    ClienteService clienteService;

    @MockBean
    BancoService bancoService;

    @MockBean
    ClienteRepository clienteRepository;

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
        when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
        try {
            Cliente clienteGuardado = clienteService.saveCliente(unCliente);
            assertEquals("12345678",clienteGuardado.getCuit());
        } catch (ClienteService.ClienteException e) {
            fail("Test no cumplido");
        }
    }

    @Test
    void testCrearClienteRiesgoBancoMal(){
        when(bancoService.verificarRiesgo(any(Cliente.class))).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
        ClienteService.ClienteNoHbilitadoException exception = assertThrows(ClienteService.ClienteNoHbilitadoException.class, () -> clienteService.saveCliente(unCliente));
        assertEquals("Error. El cliente no cumple con los requisitos de riesgo.",exception.getMessage());
    }

}
