package CSM;

import smart.theatre.distributions.Distribution;

import java.util.LinkedList;

public class Station extends AbstractStation{
    private int numServers;
    private int clientsInWaiting;
    private int servingClients;

    private int idStation;
    private Observer observer;
    private double startS;
    private Path path;
    private LinkedList<Client> waitingLine = new LinkedList<>();

    private int numClienti=0; //debug

    //todo: forse non c'è bisogno di passare un array di abstractstation, tanto queste sono stazioni pensate per inviare a una sola stazione
    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer numServers, Integer idStation, Observer observer, Double tEnd) throws IllegalArgumentException {
        if (acquaintances.length==0 || numServers<1) throw new IllegalArgumentException();
        super.send("init",d,acquaintances); //todo: verifica se funziona
        this.numServers=numServers;
        this.idStation = idStation;
        this.observer=observer;
        clientsInWaiting=0;
        servingClients=0;
        this.send(tEnd,"finish");
    }
    @Msgsrv
    public void finish(){
        observer.updateServiceTime(now()-startS);
        System.out.println("Nuovo tempo di servizio, stazione "+idStation+": "+observer.getServiceTime());
        System.out.println(numClienti); //debug
    }
    @Msgsrv
    public void setPath(Path path){
        this.path=path;
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        numClienti++; //debug
        System.out.println("Cliente "+c.getId()+" arrivato alla stazione "+idStation+". Time: "+now()); //debug
        c.setArrivalTime(now());
        switch(state){
            case FREE:
                if(path!=null) path.up(now());
                if(servingClients==0) {
                    System.out.println("Stazione "+idStation+" libera");
                    startS=now();
                }
                System.out.println("Stazione "+idStation+" libera, il Cliente "+c.getId()+" può essere servito. Time: "+now()); //debug
                this.send(d.nextSample(),"departure",c);
                servingClients++;
                if (servingClients==numServers) state=State.BUSY;
                break;
            case BUSY:
                clientsInWaiting++;
                System.out.println("Stazione "+idStation+" occupata, il Cliente "+c.getId()+" si accoda (Clienti in attesa "+clientsInWaiting+"). Time: "+now()); //debug
                waitingLine.addLast(c);
        }
    }

    @Override @Msgsrv
    public void departure(Client c) {
        if(path!=null) path.down(now());
        System.out.println("Cliente "+c.getId()+" parte dalla stazione "+idStation+". Time: "+now()); //debug
        c.setDepartureTime(now());
        observer.incrementDeparture();
        observer.updateTotalSojournTime(c.getDepartureTime()-c.getArrivalTime());
        servingClients--;
        acquaintances[0].send("arrival",c);
        switch(state){
            case FREE:
                if(servingClients==0) {
                    observer.updateServiceTime(now()-startS);
                    System.out.println("Nuovo tempo di servizio, stazione "+idStation+": "+observer.getServiceTime());
                }
                break;
            case BUSY:
                if (waitingLine.size()==0) {
                    state=State.FREE;
                    if(servingClients==0) {
                        observer.updateServiceTime(now()-startS);
                        System.out.println("Nuovo tempo di servizio, stazione "+idStation+": "+observer.getServiceTime());
                    }
                }
                else {
                    if(path!=null) path.up(now());
                    Client nextClient = waitingLine.removeFirst();
                    clientsInWaiting--;
                    System.out.println("Cliente "+nextClient.getId()+" esce dalla coda della stazione "+idStation+", pronto ad essere servito (Clienti in attesa "+clientsInWaiting+"). Time: "+now()); //debug
                    this.send(d.nextSample(),"departure",nextClient);
                    servingClients++;
                }
        }
    }

}
