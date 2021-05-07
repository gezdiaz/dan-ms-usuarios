package dan.tp2021.danmsusuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
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

import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.domain.TipoUsuario;
import dan.tp2021.danmsusuarios.domain.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import dan.tp2021.danmsusuarios.domain.Cliente;
import io.swagger.models.Response;

@RestController
@RequestMapping("/api/cliente")
@Api(value = "ClienteRest", description = "Permite gestionar los clientes de la empresa")
public class ClienteRest {
    
    public static final List<Cliente> listaClientes = new ArrayList<>();
    private static Integer ID_GEN = 1;

    @Autowired
    private ClienteService clienteServiceImpl;

    public ClienteRest(){
        super();

        //Genero una lista con Clientes aleatorios para probar
        /*Random ran = new Random();
        String[] razonesSociales = {"r1", "r2"};

        for(int i = 0; i < 5; i++){
            int ranint = ran.nextInt();
            if(ranint < 0) ranint = -ranint;

            Usuario user = new Usuario(
                    Usuario.getNextId(),
                    "user"+Integer.toString(ranint),
                    Integer.toString(ranint),
                    new TipoUsuario(1, "Cliente")
            );

            Cliente nuevo = new Cliente(
                    ID_GEN,
                    razonesSociales[ranint % 2],
                    Integer.toString(ranint),
                    "mail"+Integer.toString(ranint%159)+"@aol.com",
                    ranint/150.5,
                    true,
                    new ArrayList<Obra>(),
                    user
            );
            listaClientes.add(nuevo);
            ID_GEN++;
        }*/

    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un cliente por id")
    public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id){

        Optional<Cliente> c =  clienteServiceImpl.getListaClientes().getBody()
                .stream()
                .filter(unCli -> unCli.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> todos(@RequestParam(required = false, name = "razonSocial", defaultValue = "") String razonSocial){
    	
        List<Cliente> resultado = clienteServiceImpl.getListaClientes().getBody();
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

        Optional<Cliente> encontrado = clienteServiceImpl.getListaClientes().getBody().stream()
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
            return clienteServiceImpl.saveCliente(nuevo);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(nuevo);
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
        List<Cliente> listaDeClientes = clienteServiceImpl.getListaClientes().getBody();
        OptionalInt indexOpt =   IntStream.range(0, listaDeClientes.size())
        .filter(i -> listaDeClientes.get(i).getId().equals(id))
        .findFirst();

        if(indexOpt.isPresent()){
//            Cliente old = listaClientes.get(indexOpt.getAsInt());
//            old.merge(nuevo);
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
        return clienteServiceImpl.darDeBaja(id);
    }


}

