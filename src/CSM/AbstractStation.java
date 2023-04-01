package CSM;

import smart.theatre.distributions.Distribution;
import smart.theatre.standalone.Actor;

public abstract class AbstractStation extends Actor {

    protected enum State{
        FREE, BUSY
    }
    protected Distribution d;
    protected AbstractStation[] acquaintances;
    protected State state;


    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances) throws IllegalArgumentException {
        if (d==null) throw new IllegalArgumentException();
        state=State.FREE;
        this.d=d;
        this.acquaintances=acquaintances;
    }

    @Msgsrv
    public abstract void arrival(Client c);

    @Msgsrv
    public abstract void departure(Client c);
}
