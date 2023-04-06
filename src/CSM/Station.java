package CSM;

import smart.theatre.distributions.Distribution;

import java.util.LinkedList;

public class Station extends AbstractStation{
    private int numServers;
    private int clientsInWaiting;
    private int servingClients;

    private int idStation;
    private Observer observer;
    private Path path;
    private LinkedList<Client> waitingLine = new LinkedList<>();

    private boolean verbose;


    //todo: forse non c'è bisogno di passare un array di abstractstation, tanto queste sono stazioni pensate per inviare a una sola stazione
    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer numServers, Integer idStation, Observer observer, Double tEnd, Boolean verbose) throws IllegalArgumentException {
        if (acquaintances.length==0 || numServers<1) throw new IllegalArgumentException();
        super.send("init",d,acquaintances);
        this.verbose=verbose;
        this.numServers=numServers;
        this.idStation = idStation;
        this.observer=observer;
        clientsInWaiting=0;
        servingClients=0;
        this.send(tEnd,"finish");
    }
    @Msgsrv
    public void finish(){
        observer.endService(now());
    }
    @Msgsrv
    public void setPath(Path path){
        this.path=path;
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        if (verbose) System.out.println("Cliente "+c.getId()+" arrivato alla stazione "+idStation+". Time: "+now()); //debug
        c.setArrivalTime(now());
        switch(state){
            case FREE:
                if(path!=null) path.up(now());
                if(servingClients==0) {
                    if (verbose) System.out.println("Stazione "+idStation+" libera");
                    observer.startService(now());
                }
                if (verbose) System.out.println("Stazione "+idStation+" libera, il Cliente "+c.getId()+" può essere servito. Time: "+now()); //debug
                this.send(d.nextSample(),"departure",c);
                servingClients++;
                if (servingClients==numServers) state=State.BUSY;
                break;
            case BUSY:
                clientsInWaiting++;
                if (verbose) System.out.println("Stazione "+idStation+" occupata, il Cliente "+c.getId()+" si accoda (Clienti in attesa "+clientsInWaiting+"). Time: "+now()); //debug
                waitingLine.addLast(c);
        }
    }

    @Override @Msgsrv
    public void departure(Client c) {
        if(path!=null) path.down(now());
        if (verbose) System.out.println("Cliente "+c.getId()+" parte dalla stazione "+idStation+". Time: "+now()); //debug
        c.setDepartureTime(now());
        observer.incrementDeparture();
        observer.updateTotalSojournTime(c.getDepartureTime()-c.getArrivalTime());
        servingClients--;
        acquaintances[0].send("arrival",c);
        switch(state){
            case FREE:
                if(servingClients==0) {
                    observer.endService(now());
                }
                break;
            case BUSY:
                if (waitingLine.size()==0) {
                    state=State.FREE;
                    if(servingClients==0) {
                        observer.endService(now());
                    }
                }
                else {
                    if(path!=null) path.up(now());
                    Client nextClient = waitingLine.removeFirst();
                    clientsInWaiting--;
                    if (verbose) System.out.println("Cliente "+nextClient.getId()+" esce dalla coda della stazione "+idStation+", pronto ad essere servito (Clienti in attesa "+clientsInWaiting+"). Time: "+now()); //debug
                    this.send(d.nextSample(),"departure",nextClient);
                    servingClients++;
                }
        }
    }

}
