package CSM;

import smart.theatre.distributions.Distribution;

import java.util.LinkedList;

public class ReflectiveStation extends AbstractStation{


    private int totalClients; //Numero di clienti totali nella simulazione
    private int reflectingClients; //Numero di clienti fuori dal sistema (dentro la Reflective Station)
    private int outClients; //Numero di clienti nel sistema (dentro la Reflective Station)

    private Observer observer;
    private double startS;

    private boolean finishedGeneration;
    private static int generation=0;
    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer totalClients, Observer observer, Double tEnd) throws IllegalArgumentException {
        if (acquaintances.length==0 || totalClients<=0 || observer==null || tEnd<=0) throw new IllegalArgumentException();
        super.send("init",d,acquaintances);
        this.totalClients = totalClients;
        reflectingClients=0;
        outClients=0;
        this.observer=observer;
        finishedGeneration=false;
        Client c = new Client(generation++);
        this.send("arrival",c);
        this.send(tEnd,"finish");
    }

    @Msgsrv
    public void finish(){
        observer.updateServiceTime(now()-startS);
        System.out.println(observer.getDepartures()); //todo: debug, stampa da cacciare
    }

    //todo: possibile introduzione di un metodo generate per snellire arrival
    @Override @Msgsrv
    public void arrival(Client c) {
        if (finishedGeneration) {
            observer.incrementDeparture();
            c.setGlobalDepartureTime(now());
            observer.updateTotalSojournTime(c.getGlobalDepartureTime()-c.getGlobalArrivalTime());
        }
        if (outClients==0) { observer.updateServiceTime(now()-startS); }
        System.out.println("Cliente "+c.getId()+" inizia a pensare. Time: "+now()); //debug
        if (outClients>0) outClients--;
        reflectingClients++;
        this.send(d.nextSample(),"departure",c);
        if (totalClients>reflectingClients+outClients) {
            Client cNew = new Client(generation++);
            this.send("arrival",cNew);
        }
        else finishedGeneration=true;
    }

    @Override @Msgsrv
    public void departure(Client c) {
        if (outClients==0) startS=now();
        System.out.println("Cliente "+c.getId()+" smette di pensare. Time: "+now()); //debug
        reflectingClients--;
        outClients++;
        acquaintances[0].send("arrival",c); //invio alla stazione P1
        c.setGlobalArrivalTime(now());
    }
}
