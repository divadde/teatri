package CSM;

import smart.theatre.distributions.Distribution;

public class ReflectiveStation extends AbstractStation{
    private int totalClients; //Numero di clienti totali durante la simulazione
    private int reflectingClients; //Numero di clienti fuori dal sistema (dentro la Reflective Station) //todo: si può cacciare? sfruttiamo generation
    private int outClients; //Numero di clienti nel sistema (fuori dalla Reflective Station) //todo: si può sostituire? con una booleana

    private Observer observer;
    private static int generation=0;

    private boolean verbose;

    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer totalClients, Observer observer, Double tEnd, Boolean verbose) throws IllegalArgumentException {
        if (acquaintances.length==0 || totalClients<=0 || observer==null || tEnd<=0) throw new IllegalArgumentException();
        super.send("init",d,acquaintances);
        this.verbose=verbose;
        this.totalClients = totalClients;
        reflectingClients=0;
        outClients=0;
        this.observer=observer;
        this.send("generate");
        this.send(tEnd,"finish");
    }

    @Msgsrv
    public void finish(){
        observer.endService(now());
    }

    @Msgsrv
    public void generate(){
        Client c = new Client(generation++);
        reflectingClients++;
        if (verbose) System.out.println("Cliente "+c.getId()+" inizia a pensare. Time: "+now());
        this.send(d.nextSample(),"departure",c);
        if (totalClients>reflectingClients) {
            this.send("generate");
        }
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        observer.incrementDeparture();
        c.setGlobalDepartureTime(now());
        observer.updateTotalSojournTime(c.getGlobalDepartureTime()-c.getGlobalArrivalTime());
        outClients--;
        if (outClients==0) { observer.endService(now()); }
        if (verbose) System.out.println("Cliente "+c.getId()+" inizia a pensare. Time: "+now());
        reflectingClients++;
        this.send(d.nextSample(),"departure",c);
    }

    @Override @Msgsrv
    public void departure(Client c) {
        if (outClients==0) observer.startService(now());
        if (verbose) System.out.println("Cliente "+c.getId()+" smette di pensare. Time: "+now());
        reflectingClients--;
        outClients++;
        acquaintances[0].send("arrival",c); //invio alla stazione P1
        c.setGlobalArrivalTime(now());
    }
}
