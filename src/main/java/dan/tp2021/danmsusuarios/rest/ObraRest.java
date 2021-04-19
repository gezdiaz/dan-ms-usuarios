package dan.tp2021.danmsusuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.domain.TipoObra;

@RestController
@RequestMapping("/api/obra")
public class ObraRest {

    public static List<Obra> listaObras = new ArrayList<>();
    private static int ID_GEN = 1;

    public ObraRest(){
        super();

        //Genero una lista con Obras aleatorias para probar

        Random ran = new Random();

        for(int i = 0; i < 20; i++){
            int ranint = ran.nextInt();
            if(ranint < 0) ranint = -ranint;

            Cliente cliente = ClienteRest.listaClientes.get(ranint % ClienteRest.listaClientes.size());
            TipoObra tipo = new TipoObra();
            switch (ranint % 4){
                case 0:
                    tipo.setId(1);
                    tipo.setDescriocion("Reforma");
                    break;
                case 1:
                    tipo.setId(2);
                    tipo.setDescriocion("Casa");
                    break;
                case 2:
                    tipo.setId(3);
                    tipo.setDescriocion("Edificio");
                    break;
                case 3:
                    tipo.setId(4);
                    tipo.setDescriocion("Vial");
                    break;
            }

            Obra nuevo = new Obra(
                    ID_GEN,
                    "Descripción"+ranint,
                    Float.parseFloat(Double.toString((ranint % 1000) * 1.0)),
                    Float.parseFloat(Double.toString((ranint % 1000) * 0.5)),
                    "Dirección"+ranint,
                    ranint,
                    tipo,
                    cliente
            );
            cliente.getObras().add(nuevo);
            listaObras.add(nuevo);
            ID_GEN++;
        }

    }


    @GetMapping()
    public ResponseEntity<List<Obra>> todos(@RequestParam(required = false, defaultValue = "0") Integer idCliente, @RequestParam(required = false, defaultValue = "0") Integer idTipoObra, @RequestParam(required = false, defaultValue = "") String cuitCliente){

        List<Obra> resultado = listaObras;

        if(idCliente > 0){
            resultado = resultado.stream()
                    .filter(obra -> obra.getCliente().getId().equals(idCliente))
                    .collect(Collectors.toList());
        }
        if(idTipoObra > 0){
            resultado = resultado.stream()
                    .filter(obra -> obra.getTipo().getId().equals(idTipoObra))
                    .collect(Collectors.toList());
        }

        if(!cuitCliente.isEmpty()){
            resultado = resultado.stream()
                    .filter(obra -> obra.getCliente().getCuit().equals(cuitCliente))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Obra> obraPorId(@PathVariable(name = "id") Integer id){
        Optional<Obra> res = listaObras.stream()
                .filter(obra -> obra.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(res);
    }

    @PostMapping()
    public ResponseEntity<Obra> crear(@RequestBody() Obra nuevo){
        nuevo.setId(ID_GEN++);
        listaObras.add(nuevo);
        //TODO ver que pasa con el cliente. Hay que crear uno nuevo o verificar que existe el recibido? Se recibe un cliente completo un ID?
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Obra> actualizar(@PathVariable(name = "id") Integer id ,@RequestBody Obra nuevo){
        //me fijo si el id existe en la lista de obras
        OptionalInt indexOpt =   IntStream.range(0, listaObras.size())
                .filter(i -> listaObras.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            //El id existe
//            Obra old = listaObras.get(indexOpt.getAsInt());
//            old.merge(nuevo);
            //no es necesario agregarlo a lo lista, pero lo dejo como recordatorio de que acá habría que guardar el empleado en la DB (o el controller tendría que hacerlo)
            nuevo.setId(id);
            listaObras.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        }else{
            //NO existe
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Obra> borrar(@PathVariable(name = "id") Integer id){
        //me fijo si el id existe en la lista de obras
        OptionalInt indexOpt =   IntStream.range(0, listaObras.size())
                .filter(i -> listaObras.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            Obra old = listaObras.get(indexOpt.getAsInt());
            listaObras.remove(indexOpt.getAsInt());
            return ResponseEntity.ok(old);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

}
