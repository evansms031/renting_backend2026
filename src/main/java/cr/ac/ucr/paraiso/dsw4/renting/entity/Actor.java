package cr.ac.ucr.paraiso.dsw4.renting.entity;

import jakarta.persistence.*;

@Entity
@Table(name="actor")
public class Actor {

    // define and annotate fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="actor_id")
    private int actorId;

    @Column(name="nombre_actor")
    private String nombreActor;

    @Column(name="apellidos_actor")
    private String apellidosActor;

    public Actor() {

    }

    public Actor(String nombreActor, String apellidosActor) {
        this.nombreActor = nombreActor;
        this.apellidosActor = apellidosActor;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getNombreActor() {
        return nombreActor;
    }

    public void setNombreActor(String nombreActor) {
        this.nombreActor = nombreActor;
    }

    public String getApellidosActor() {
        return apellidosActor;
    }

    public void setApellidosActor(String apellidosActor) {
        this.apellidosActor = apellidosActor;
    }

}

