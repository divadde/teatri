package CSM_Little_Law;

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
    public abstract void arrival();

    @Msgsrv
    public abstract void departure();
}
