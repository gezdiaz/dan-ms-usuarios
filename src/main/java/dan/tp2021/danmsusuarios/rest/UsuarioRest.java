package dan.tp2021.danmsusuarios.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import dan.tp2021.danmsusuarios.config.KeycloakUrlConfig;
import dan.tp2021.danmsusuarios.login.Token;
import dan.tp2021.danmsusuarios.login.Usuario;

@RestController
@RequestMapping("/login")
public class UsuarioRest {
	
	private static final Logger logger =  LoggerFactory.getLogger(UsuarioRest.class);
	
	@Autowired
	KeycloakUrlConfig urlKeycloak;
	@PostMapping()
	public ResponseEntity<?> login(@RequestBody Usuario usuario) {
		
		String url = urlKeycloak.getUrl();
		logger.debug(url);
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("client_id", "dan-client");
		formData.add("grant_type", "password");
		formData.add("username", usuario.getUsername());
		formData.add("password", usuario.getPassword());
		formData.values().forEach(s -> s.forEach(s1-> logger.debug(s1)));
		
		try {
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded");
			HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String,String>>(formData, headers);
			ResponseEntity<Token> resp = restTemplate.exchange(url+"/auth/realms/dan-realm/protocol/openid-connect/token", HttpMethod.POST, body, Token.class);
			
			return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
			
		}
		catch(HttpClientErrorException.BadRequest e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch(HttpClientErrorException.Unauthorized e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
		catch(HttpClientErrorException.Forbidden e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
		catch(Exception e) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
