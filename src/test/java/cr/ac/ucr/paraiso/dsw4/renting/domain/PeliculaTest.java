package cr.ac.ucr.paraiso.dsw4.renting.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PeliculaTest {

    //Un metodo bajo prueba es una unidad de comportamiento, se comporta de muchas maneras
    @Test
    public void whenPeliculaIsCreatedWithAllParametersConstructor_thenPeliculaIsNotNull(){

        //Arrange
        Genero genero = new Genero(1,"Acción");
        Actor actor = new Actor(1, "Tom", "Cruise");
        List<Actor> actores = new ArrayList<Actor>();
        actores.add(actor);
        Pelicula pelicula; 

        //Act
        pelicula = new Pelicula(1, "Misión Imposible", true,true, genero, actores);

        //Assert
        assertNotNull(pelicula);
    }
    

}
