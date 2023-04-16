package CSM;

import smart.theatre.distributions.Distribution;

public class ReflectiveStation extends AbstractStation{
    private Observer observer;
    private static int generation=0;
    private boolean verbose;

    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer totalClients, Observer observer, Double tEnd, Boolean verbose) throws IllegalArgumentException {
        if (acquaintances.length==0 || totalClients<=0 || observer==null || tEnd<=0) throw new IllegalArgumentException();
        super.send("init",d,acquaintances);
        this.verbose=verbose;
        this.observer=observer;
        this.send("generate",totalClients);
    }

    @Msgsrv
    public void generate(Integer totalClients){
        Client c = new Client(++generation);
        if (verbose) System.out.println("Cliente "+c.getId()+" inizia a pensare. Time: "+now());
        this.send(d.nextSample(),"departure",c);
        if (totalClients>generation) {
            this.send("generate", totalClients);
        }
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        observer.incrementDeparture();
        observer.updateTotalSojournTime(now()-c.getGlobalArrivalTime());
        observer.updateServiceTime(now()-c.getGlobalArrivalTime());
        if (verbose) System.out.println("Cliente "+c.getId()+" inizia a pensare. Time: "+now());
        this.send(d.nextSample(),"departure",c);
    }

    @Override @Msgsrv
    public void departure(Client c) {
        if (verbose) System.out.println("Cliente "+c.getId()+" smette di pensare. Time: "+now());
        acquaintances[0].send("arrival",c); //invio alla stazione P1
        c.setGlobalArrivalTime(now());
    }
}
