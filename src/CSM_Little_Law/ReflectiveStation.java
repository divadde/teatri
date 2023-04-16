package CSM_Little_Law;

import smart.theatre.distributions.Distribution;

public class ReflectiveStation extends AbstractStation {
    private Observer observer;
    private static int generation=0;
    private boolean verbose;
    private Path path;

    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer totalClients, Observer observer, Boolean verbose) throws IllegalArgumentException {
        if (acquaintances.length==0 || totalClients<=0 || observer==null) throw new IllegalArgumentException();
        super.send("init",d,acquaintances);
        this.verbose=verbose;
        this.observer=observer;
        this.path = new Path();
        observer.setPath(this.path);
        this.send("generate",totalClients);
    }

    @Msgsrv
    public void generate(Integer totalClients){
        ++generation;
        if (verbose) System.out.println("Un cliente inizia a pensare. Time: "+now());
        this.send(d.nextSample(),"departure");
        if (totalClients>generation) {
            this.send("generate", totalClients);
        }
    }

    @Override @Msgsrv
    public void arrival() {
        path.down(now());
        observer.incrementDeparture();
        if (verbose) System.out.println("Un cliente inizia a pensare. Time: "+now());
        this.send(d.nextSample(),"departure");
    }

    @Override @Msgsrv
    public void departure() {
        path.up(now());
        if (verbose) System.out.println("Un cliente smette di pensare. Time: "+now());
        acquaintances[0].send("arrival"); //invio alla stazione P1
    }
}
