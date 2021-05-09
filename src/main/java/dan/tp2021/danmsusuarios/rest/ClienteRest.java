package dan.tp2021.danmsusuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dan.tp2021.danmsusuarios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import dan.tp2021.danmsusuarios.domain.Cliente;

@RestController
@RequestMapping("/api/cliente")
@Api(value = "ClienteRest", description = "Permite gestionar los clientes de la empresa")
public class ClienteRest {

    @Autowired
    private ClienteService clienteServiceImpl;


    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un cliente por id")
    public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id){

        Optional<Cliente> c =  clienteServiceImpl.getListaClientes()
                .stream()
                .filter(unCli -> unCli.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> todos(@RequestParam(required = false, name = "razonSocial", defaultValue = "") String razonSocial){
    	
        List<Cliente> resultado = clienteServiceImpl.getListaClientes();
        //4.a.ii filtrar por razon social con un parametro opcional.
        if(razonSocial.length() > 0) {
            resultado = resultado.stream()
                    .filter(cliente -> cliente.getRazonSocial().contains(razonSocial))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(resultado);
    }

    //4.a.i GET cliente by cuit
    @GetMapping(path = "/cuit/{cuit}")
    public ResponseEntity<Cliente> clientePorCuit(@PathVariable(name = "cuit") String cuit){

        System.out.println("Parametros recibidos: \ncuit: "+cuit);

        Optional<Cliente> encontrado = clienteServiceImpl.getListaClientes()
                .stream()
                .filter(cliente -> cliente.getCuit().equals(cuit))
                .findFirst();

        return ResponseEntity.of(encontrado);
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente nuevo){
    	System.out.println(" crear cliente "+nuevo);

    	if(nuevo.getUser().getPassword()!=null && nuevo.getUser().getUser()!=null && nuevo.getObras()!=null && nuevo.getObras().size()>0){

            //nuevo.setId(ID_GEN++);
            //listaClientes.add(nuevo);
            try {
                Cliente guardado = clienteServiceImpl.saveCliente(nuevo);
                return ResponseEntity.ok(guardado);
            } catch (ClienteService.ClienteNoHbilitadoException e) {
                //El cliente no esta habilitado, retorno 400 porque es un error de los datos, no es un error del servidor.TODO ver si puede ser un 403 FORBIDDEN
                System.out.println("Cliente no habilitado. Mensaje de la excepción: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            } catch (ClienteService.ClienteException e) {
                //500 internal server error, porque es un eror del servidor, también podría ser un 503 (Servicio no disponible) o un 502 (GBad gateway)
                System.out.println("Error al guardar el cliente. Mensaje de la excepción: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    	//Si los datos recibidos no cumplen las condiciones -> BAD REQUEST, no hay que enviar el cliente porque no es válido.
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza un cliente")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Actualizado correctamente"),
        @ApiResponse(code = 401, message = "No autorizado"),
        @ApiResponse(code = 403, message = "Prohibido"),
        @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Cliente> actualizar(@RequestBody Cliente nuevo,  @PathVariable Integer id){
        List<Cliente> listaDeClientes = clienteServiceImpl.getListaClientes();
        OptionalInt indexOpt =   IntStream.range(0, listaDeClientes.size())
        .filter(i -> listaDeClientes.get(i).getId().equals(id))
        .findFirst();

        if(indexOpt.isPresent()){
//            Cliente old = listaClientes.get(indexOpt.getAsInt());
//            old.merge(nuevo);
            //TODO cambiar para usar el service.
            nuevo.setId(id);
            listaDeClientes.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Cliente> borrar(@PathVariable Integer id){
        /*List<Cliente> listaDeClientes = clienteServiceImpl.getListaClientes().getBody();
        OptionalInt indexOpt =   IntStream.range(0, listaDeClientes.size())
        .filter(i -> listaDeClientes.get(i).getId().equals(id))
        .findFirst();

        if(indexOpt.isPresent()){
            //listaDeClientes.remove(indexOpt.getAsInt());
            clienteServiceImpl.darDeBaja()
            //TODO que pasa con las obras de este cliente? Se tiene que eliminar? O quedan "huérfanas"?
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }*/
        try {
            Cliente eliminado = clienteServiceImpl.darDeBaja(id);
            return ResponseEntity.ok(eliminado);
        } catch (ClienteService.ClienteNotFoundException e) {
            //El cliente no esta habilitado, retorno 400 porque es un error de los datos, no es un error del servidor.TODO ver si puede ser un 403 FORBIDDEN
            System.out.println("Error Cliente a eliminar no encontrado. Mensaje de la excepción: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (ClienteService.ClienteException e) {
            //500 internal server error, porque es un eror del servidor, también podría ser un 503 (Servicio no disponible) o un 502 (GBad gateway)
            System.out.println("Error al dar de baja el cliente. Mensaje de la excepción: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

