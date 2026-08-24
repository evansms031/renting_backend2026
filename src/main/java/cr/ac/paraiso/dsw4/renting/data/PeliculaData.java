package cr.ac.paraiso.dsw4.renting.data;

import org.springframework.stereotype.Repository;

import ch.qos.logback.core.joran.action.Action;
import cr.ac.paraiso.dsw4.renting.domain.Actor;
import cr.ac.paraiso.dsw4.renting.domain.Genero;
import cr.ac.paraiso.dsw4.renting.domain.Pelicula;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

@Repository
public class PeliculaData {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Pelicula> findMovieByTitleOrGenre(String title, String genre){
    String sqlSelect = """
            SELECT
                p.pelicula_id,
                p.titulo,
                p.genero_id,
                g.nombre_genero,
                p.subtitulada,
                p.estreno,
                pa.actor_id,
                a.nombre_actor,
                a.apellidos_actor
            FROM Pelicula p
            INNER JOIN Genero g
                ON p.genero_id = g.genero_id
            LEFT JOIN PeliculaActor pa
                ON p.pelicula_id = pa.pelicula_id
            LEFT JOIN Actor a
                ON pa.actor_id = a.actor_id
            WHERE LOWER(p.titulo) LIKE ?
            or LOWER(g.nombre_genero) LIKE ?
            """;

            //title = title.toLowerCase();
            //genre = genre.toLowerCase();

            String titleLike =  (title == null || title == "" ? "" : "%" + title.trim() + "%");
            String genreLike =  (genre == null || genre == "" ? "" : "%" + genre.trim() + "%");

            return jdbcTemplate.query(sqlSelect, new PeliculaExtractor(), titleLike,genreLike);
    }

}

class PeliculaExtractor implements ResultSetExtractor<List<Pelicula>>{

    @Override
    public List<Pelicula> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Pelicula> map = new HashMap<>();
        Pelicula pelicula = null;
        while(rs.next()){ //Le pregunta al resultSet so tiene registros por recorrer
            int peliculaId = rs.getInt("pelicula_id");
            pelicula = map.get(peliculaId);
            if(pelicula == null){
                pelicula = new Pelicula();
                pelicula.setPeliculaId(peliculaId);
                pelicula.setTitulo(rs.getString("titulo"));
                Genero genero = new Genero();
                genero.setGeneroId(rs.getInt("genero_id"));
                genero.setNombreGenero(rs.getString("nombre_genero"));
                pelicula.setGenero(genero);
                pelicula.setSubtitulada(rs.getBoolean("subtitulada"));
                pelicula.setEstreno(rs.getBoolean("estreno"));
                map.put(peliculaId, pelicula);
            } //if
            int actorId = rs.getInt("actor_id");
            if (actorId > 0) {
                Actor actor = new Actor();
                actor.setActorId(actorId);
                actor.setNombreActor(rs.getString("nombre_actor"));
                actor.setApellidoActor(rs.getString("apellidos_Actor"));
                pelicula.getActores().add(actor); //ojo
            } // if
        } // while
        return new ArrayList<Pelicula>(map.values());
    }
    
}
