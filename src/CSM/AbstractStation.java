package CSM;

import smart.theatre.distributions.Distribution;
import smart.theatre.standalone.Actor;

public abstract class AbstractStation extends Actor {

    //todo, vanno bene con protected? o vanno private?
    protected enum State{
        FREE, BUSY
    }
    protected Distribution d;
    protected AbstractStation[] acquaintances;
    protected State state;

    //todo: associare anche degli observers? (tipo Path)

    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances) throws IllegalArgumentException {
        if(acquaintances.length==0) throw new IllegalArgumentException();
        state=State.FREE;
        this.d=d;
        this.acquaintances=acquaintances;
    }

    @Msgsrv
    public abstract void arrival(Client c);

    @Msgsrv
    public abstract void departure(Client c);
}
