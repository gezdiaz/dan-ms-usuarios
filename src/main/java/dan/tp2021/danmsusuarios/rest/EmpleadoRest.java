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

import dan.tp2021.danmsusuarios.domain.Empleado;
import dan.tp2021.danmsusuarios.domain.TipoUsuario;
import dan.tp2021.danmsusuarios.domain.Usuario;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoRest {

    //TODO cambiar todo para usar un repo
    private static List<Empleado> listaEmpleados = new ArrayList<>();
    private static Integer ID_GEN = 1;


    @GetMapping()
    public ResponseEntity<List<Empleado>> todos(@RequestParam(name = "nombre", required = false, defaultValue = "") String nombre){

        List<Empleado> resultado = listaEmpleados;

        if(nombre.length() > 0){
            resultado = listaEmpleados.stream()
                    .filter(empleado -> empleado.getNombre().equals(nombre))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> empleadoPorId(@PathVariable(name = "id") Integer id){

        Optional<Empleado> res = listaEmpleados.stream()
                .filter(empleado -> empleado.getId().equals(id))
                .findFirst();

        return ResponseEntity.of(res);
    }

    @PostMapping()
    public ResponseEntity<Empleado> crear(@RequestBody() Empleado nuevo){
        nuevo.setId(ID_GEN++);//TODO no dejar que lo cree si el nombre es null
        listaEmpleados.add(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable(name = "id") Integer id, @RequestBody() Empleado nuevo){

        //TODO probar con la base de datos, porque ahora cambia todos los valores por cada atributo que trae "Empleado nuevo". Incluyendo valores nulos.
        
        //me fijo si el id existe en la lista de empleados
        OptionalInt indexOpt =   IntStream.range(0, listaEmpleados.size())
                .filter(i -> listaEmpleados.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            //el id existe
//            Empleado old = listaEmpleados.get(indexOpt.getAsInt());
//            //el empleado recibido puede no tener to-do seteado, solo actualizo los parámetros que están seteados.
            // no hace falta hacer esto, porque
//            old.merge(nuevo);
            //no es necesario agregarlo a lo lista, pero lo dejo como recordatorio de que acá habría que guardar el empleado en la DB (o el controller tendría que hacerlo)
            nuevo.setId(id);
            listaEmpleados.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        }else{
            // el id no existe
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Empleado> borrar(@PathVariable(name = "id") Integer id){
        //me fijo si el id exsiste en la lista de empleados
        OptionalInt indexOpt =   IntStream.range(0, listaEmpleados.size())
                .filter(i -> listaEmpleados.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            //el id existe
            Empleado old = listaEmpleados.get(indexOpt.getAsInt());
            listaEmpleados.remove(indexOpt.getAsInt());
            return ResponseEntity.ok(old);
        }else{
            // el id no existe
            return ResponseEntity.notFound().build();
        }

    }



}
