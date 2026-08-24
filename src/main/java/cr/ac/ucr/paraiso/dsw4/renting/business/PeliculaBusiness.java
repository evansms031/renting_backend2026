package cr.ac.ucr.paraiso.dsw4.renting.business;

import java.sql.DataTruncation;
import java.util.List;

import org.springframework.stereotype.Service;

import cr.ac.ucr.paraiso.dsw4.renting.data.PeliculaData;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;

@Service
public class PeliculaBusiness {
    private final PeliculaData peliculaData;

    public PeliculaBusiness(PeliculaData peliculaData) {
        this.peliculaData = peliculaData;
    }

    public List<Pelicula> findMoviesByTitleOrGenre(String title, String genre){
        return peliculaData.findMovieByTitleOrGenre(title, genre);
    }
    

}
