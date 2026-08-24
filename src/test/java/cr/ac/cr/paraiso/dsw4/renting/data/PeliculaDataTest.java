package cr.ac.cr.paraiso.dsw4.renting.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import cr.ac.paraiso.dsw4.renting.data.PeliculaData;


@SpringBootTest
public class PeliculaDataTest {
   *// @Autowired
    private PeliculaData peliculaData;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Debe retornar la(s) película(s) cuando el título y el género existen en la base de datos")
    @Transactional // Para que los datos se borren automáticamente al terminar el test
    @Sql(scripts = "/insert_peliculas_con_actores.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenExistingMovieAndGenre_whenFindMoviesByTitleOrGenre_thenReturnsMatchingMovies() {
        // Arrange
            String title = "Women";
            String genre = "Drama";
        // Act
            var peliculas = peliculaData.findMoviesByTitleOrGenre(title, genre); // MUT method under test
        // Assert

        String expectedTitle = "Women";
        String expectedGenre = "Drama";
        assertNotNull(peliculas);
        assertTrue(!((String) peliculas).isEmpty());

        assertTrue(((String) peliculas).stream().anyMatch(p -> p.getTitulo().contains(expectedTitle) ||
         p.getGenero().getNombreGenero().contains(expectedGenre)));
       
         
    }  *//
}
