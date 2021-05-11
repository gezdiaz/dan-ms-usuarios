package dan.tp2021.danmsusuarios;

import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.domain.Usuario;
import dan.tp2021.danmsusuarios.service.BancoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PedidoRestTest {
    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    @LocalServerPort String puerto;

    Cliente unCliente;

    @BeforeEach
    void setUp(){
        unCliente = new Cliente();
        unCliente.setId(1);
        unCliente.setCuit("12345678");
        unCliente.setMail("mail@mail.com");
        unCliente.setRazonSocial("Nombre");
        Usuario unUsuario = new Usuario();
        unUsuario.setPassword("123456");
        unUsuario.setUser("Usuario de prueba 1");
        unCliente.setUser(unUsuario);
        Obra unaObra = new Obra();
        List<Obra> listaObras = new ArrayList<>();
        listaObras.add(unaObra);
        unCliente.setObras(listaObras);
    }

    @Test
    void todoCorrecto(){
        String server = "http://localhost:"+puerto+"/api/cliente";
        HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
        ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente,String.class);
        assertEquals(ResponseEntity.ok().build().getStatusCode(),response.getStatusCode());
    }

    @Test
    void userSinPassword(){
        unCliente.getUser().setPassword(null);
        String server = "http://localhost:"+puerto+"/api/cliente";
        HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
        ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente,String.class);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(),response.getStatusCode());
    }

    @Test
    void userSinUserName(){
        unCliente.getUser().setUser(null);
        String server = "http://localhost:"+puerto+"/api/cliente";
        HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
        ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente,String.class);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(),response.getStatusCode());
    }

    @Test
    void sinObras(){
        unCliente.getObras().remove(0);
        String server = "http://localhost:"+puerto+"/api/cliente";
        HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
        ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente,String.class);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(),response.getStatusCode());
    }

}
