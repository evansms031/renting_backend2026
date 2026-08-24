package cr.ac.ucr.paraiso.dsw4.renting.domain;

public class Actor {
    private int actorId;
    private String nombreActor;
    private String apellidoActor;

    public Actor(){
        
    }

    public Actor(int actorId, String nombreActor, String apellidoActor) {
        this.actorId = actorId;
        this.nombreActor = nombreActor;
        this.apellidoActor = apellidoActor;
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

    public String getApellidoActor() {
        return apellidoActor;
    }

    public void setApellidoActor(String apellidoActor) {
        this.apellidoActor = apellidoActor;
    }
    
    
    

}
