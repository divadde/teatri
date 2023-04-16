package CSM;

import smart.theatre.distributions.Distribution;

import java.util.LinkedList;

public class Station extends AbstractStation{
    private int numServers;
    private int servingClients;

    private int idStation;
    private Observer observer;
    private Path path;
    private LinkedList<Client> waitingLine = new LinkedList<>();

    private boolean verbose;

    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer numServers, Integer idStation, Observer observer, Boolean verbose) throws IllegalArgumentException {
        if (acquaintances.length == 0 || numServers < 1 || observer == null)
            throw new IllegalArgumentException();
        super.send("init", d, acquaintances);
        this.verbose = verbose;
        this.numServers = numServers;
        this.idStation = idStation;
        this.observer = observer;
        servingClients = 0;
    }

    @Msgsrv
    public void setPath(Path path){
        this.path=path;
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        if (verbose) System.out.println("Cliente "+c.getId()+" arrivato alla stazione "+idStation+". Time: "+now());
        c.setArrivalTime(now());
        switch(state){
            case FREE: //Una stazione k-server è FREE finché può servire clients
                if(path!=null) path.up(now());
                if(verbose) System.out.println("Stazione "+idStation+" libera, il Cliente "+c.getId()+" può essere servito. Time: "+now()); //debug
                c.setStartService(now());
                this.send(d.nextSample(),"departure",c);
                servingClients++;
                if(servingClients==numServers) state=State.BUSY;
                break;
            case BUSY:
                if (verbose) System.out.println("Stazione "+idStation+" occupata, il Cliente "+c.getId()+" si accoda (Clienti in attesa "+waitingLine.size()+"). Time: "+now()); //debug
                waitingLine.addLast(c);
        }
    }

    @Override @Msgsrv
    public void departure(Client c) {
        if (path!=null) path.down(now());
        if (verbose) System.out.println("Cliente "+c.getId()+" parte dalla stazione "+idStation+". Time: "+now()); //debug
        observer.updateTotalSojournTime(now()-c.getArrivalTime());
        observer.incrementDeparture();
        observer.updateServiceTime(now()-c.getStartService());
        servingClients--;
        if (state==State.BUSY) {
            if (waitingLine.size()==0) {
                state=State.FREE;
            }
            else {
                if(path!=null) path.up(now());
                Client nextClient = waitingLine.removeFirst();
                nextClient.setStartService(now());
                if (verbose) System.out.println("Cliente "+nextClient.getId()+" esce dalla coda della stazione "+idStation+", pronto ad essere servito (Clienti in attesa "+waitingLine.size()+"). Time: "+now()); //debug
                this.send(d.nextSample(),"departure",nextClient);
                servingClients++;
            }
        }
        acquaintances[0].send("arrival",c);
    }

}
