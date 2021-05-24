package dan.tp2021.danmsusuarios;

import dan.tp2021.danmsusuarios.dao.ClienteRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.domain.TipoObra;
import dan.tp2021.danmsusuarios.domain.Usuario;
import dan.tp2021.danmsusuarios.dto.PedidoDTO;
import dan.tp2021.danmsusuarios.service.ObraService;
import dan.tp2021.danmsusuarios.service.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteRestTest {
	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	String puerto;

	Cliente unCliente;

	List<PedidoDTO> listaPedido;
	@MockBean
	PedidoService pedidoService;

	@MockBean
	ClienteRepository clienteRepository;

	@MockBean
	ObraService obraService;

	@BeforeEach
	void setUp() {
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
		unaObra.setId(1);
		unaObra.setTipo(new TipoObra(1, "Tipo Falso"));
		List<Obra> listaObras = new ArrayList<>();
		listaObras.add(unaObra);
		unCliente.setObras(listaObras);
		listaPedido = new ArrayList<>();
		PedidoDTO pedido = new PedidoDTO();
		pedido.setId(1);
		listaPedido.add(pedido);
	}

	@Test
	void todoCorrecto() {
		String server = "http://localhost:" + puerto + "/api/cliente";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void userSinPassword() {
		unCliente.getUser().setPassword(null);
		String server = "http://localhost:" + puerto + "/api/cliente";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void userSinUserName() {
		unCliente.getUser().setUser(null);
		String server = "http://localhost:" + puerto + "/api/cliente";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void sinObras() {
		unCliente.getObras().remove(0);
		String server = "http://localhost:" + puerto + "/api/cliente";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> response = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void darDeBajaOK() {

		String server = "http://localhost:" + puerto + "/api/cliente/1";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);

		// Mock de la comunicacion entre APIs cliente y pedido
		when(pedidoService.getPedidoByClienteId(any(Integer.class))).thenReturn(listaPedido);
		// Mock de la comunicacion con la base de datos.
		when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.of(unCliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
		doNothing().when(clienteRepository).delete(any(Cliente.class));

		// Request a cliente REST
		ResponseEntity<Cliente> response = testRestTemplate.exchange(server, HttpMethod.DELETE, requestCliente,
				Cliente.class);

		assertNotNull(response.getBody().getFechaBaja());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	void darDeBajaOKSinPedidos() {

		String server = "http://localhost:" + puerto + "/api/cliente/1";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);

		// Mock de la comunicacion entre APIs cliente y pedido
		when(pedidoService.getPedidoByClienteId(any(Integer.class))).thenReturn(new ArrayList<PedidoDTO>());
		// Mock de la comunicacion con la base de datos.
		when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.of(unCliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(unCliente);
		doNothing().when(clienteRepository).delete(any(Cliente.class));

		// Request a cliente REST
		ResponseEntity<Cliente> response = testRestTemplate.exchange(server, HttpMethod.DELETE, requestCliente,
				Cliente.class);

		assertNull(response.getBody().getFechaBaja());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	void darDeBajaNoExisteCliente() {

		unCliente = null;
		String server = "http://localhost:" + puerto + "/api/cliente/1";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);

		// Mock de la comunicacion entre APIs cliente y pedido
		when(pedidoService.getPedidoByClienteId(any(Integer.class))).thenReturn(new ArrayList<PedidoDTO>());
		// Mock de la comunicacion con la base de datos.
		when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(unCliente));

		// Request a cliente REST
		ResponseEntity<Cliente> response = testRestTemplate.exchange(server, HttpMethod.DELETE, requestCliente,
				Cliente.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}
	
	@Test
	void darDeBajaError5xx() {

		
		String server = "http://localhost:" + puerto + "/api/cliente/1";
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);

		// Mock de falla en la comunicacion entre APIs cliente y pedido/
		when(pedidoService.getPedidoByClienteId(any(Integer.class))).thenReturn(null);
		// Mock de la comunicacion con la base de datos.
		when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(unCliente));

		// Request a cliente REST
		ResponseEntity<Cliente> response = testRestTemplate.exchange(server, HttpMethod.DELETE, requestCliente,
				Cliente.class);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

	}

	@Test
	void getListaClientesOK() {
		List<Cliente> lista = new ArrayList<>();
		lista.add(unCliente);
		when(clienteRepository.findByFechaBajaNullOrFechaBaja(any(Instant.class))).thenReturn(lista);
		String server = "http://localhost:" + puerto + "/api/cliente";
		ResponseEntity<List> response = testRestTemplate.exchange(server, HttpMethod.GET, HttpEntity.EMPTY,
				List.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1,response.getBody().size());
	}

	@Test
	void getListaClientesError5xx() {
		List<Cliente> lista = new ArrayList<>();
		lista.add(unCliente);
		when(clienteRepository.findByFechaBajaNullOrFechaBaja(any(Instant.class))).thenThrow(NullPointerException.class);
		String server = "http://localhost:" + puerto + "/api/cliente";
		ResponseEntity<List> response = testRestTemplate.exchange(server, HttpMethod.GET, HttpEntity.EMPTY,
				List.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}
