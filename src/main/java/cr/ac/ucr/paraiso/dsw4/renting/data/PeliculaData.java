package cr.ac.ucr.paraiso.dsw4.renting.data;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.joran.action.Action;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Actor;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Genero;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

@Repository
public class PeliculaData {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    
    public PeliculaData (DataSource dataSource){
        this.dataSource = dataSource;
    }
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
      // @Transactional es para lo del try and catch pero implicito 
      // The @Transactional annotation bounds the method execution in a transaction context as it is performing a database update.
    public Pelicula save(Pelicula pelicula) throws SQLException{
        
        Connection conexion = null;
        try {
            conexion = dataSource.getConnection();
            conexion.setAutoCommit(false);
            SimpleJdbcCall simpleJdbcCallPelicula = new SimpleJdbcCall(jdbcTemplate).
                    withCatalogName("dbo").
                    withProcedureName("Pelicula_Insert").withoutProcedureColumnMetaDataAccess().
                    declareParameters(new SqlOutParameter("@pelicula_id", Types.INTEGER)).
                    declareParameters(new SqlParameter("@titulo", Types.VARCHAR)).
                    declareParameters(new SqlParameter("@subtitulada", Types.BIT)).
                    declareParameters(new SqlParameter("@estreno", Types.BIT)).
                    declareParameters(new SqlParameter("@genero_id", Types.INTEGER));
            Map<String, Object> outParameters = simpleJdbcCallPelicula.execute(pelicula.getTitulo(), pelicula.isSubtitulada(), pelicula.isEstreno(), pelicula.getGenero().getGeneroId());
            pelicula.setPeliculaId(Integer.parseInt(outParameters.get("@pelicula_id").toString()));
           
            SimpleJdbcCall simpleJdbcCallPeliculaActor = new SimpleJdbcCall(jdbcTemplate).
                    withCatalogName("dbo").
                    withProcedureName("PeliculaActor_Insert").withoutProcedureColumnMetaDataAccess().
                    declareParameters(new SqlParameter("@pelicula_id", Types.INTEGER)).
                    declareParameters(new SqlParameter("@actor_id", Types.INTEGER));
            for(Actor actor:pelicula.getActores())
                simpleJdbcCallPeliculaActor.execute(pelicula.getPeliculaId(), actor.getActorId());
            conexion.commit(); //IMPORTANTE, aqui se consolidan todos los cmabios
        }// try
        catch (SQLException e) {
            if (conexion != null) {
                    conexion.rollback();
               
            }// if
            throw e;
        } finally { //despues del try o catch si capta un error
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        }// finally
            
        return pelicula;
    }//para la ia pasarle basado en el procedimiento almacenado para llenar una tabla y basado en los valores de una tabla donde utilice la clase simplejdbccall para crear un metodo(que las variables o parametros sea con el @)

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
