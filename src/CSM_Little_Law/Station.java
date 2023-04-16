package CSM_Little_Law;

import smart.theatre.distributions.Distribution;


public class Station extends AbstractStation {
    private int numServers;
    private int servingClients;
    private int waitingClients;

    private int idStation;
    private Observer observer;
    private Path path;
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
        this.path= new Path();
        observer.setPath(this.path);
        servingClients = 0;
        waitingClients = 0;
    }

    @Override @Msgsrv
    public void arrival() {
        if (verbose) System.out.println("Un cliente è arrivato alla stazione "+idStation+". Time: "+now());
        path.up(now());
        switch(state){
            case FREE: //Una stazione k-server è FREE finché può servire clients
                if(verbose) System.out.println("Stazione "+idStation+" libera, un cliente può essere servito. Time: "+now()); //debug
                double serviceTime = d.nextSample();
                this.send(serviceTime,"departure");
                servingClients++;
                observer.updateServiceTime(serviceTime);
                if(servingClients==numServers) state=State.BUSY;
                break;
            case BUSY:
                if (verbose) System.out.println("Stazione "+idStation+" occupata, un cliente si accoda (Clienti in attesa "+waitingClients+"). Time: "+now()); //debug
                waitingClients++;
        }
    }

    @Override @Msgsrv
    public void departure() {
        if (verbose) System.out.println("Un cliente parte dalla stazione "+idStation+". Time: "+now()); //debug
        path.down(now());
        observer.incrementDeparture();
        servingClients--;
        if (state==State.BUSY) {
            if (waitingClients==0) {
                state=State.FREE;
            }
            else {
                waitingClients--;
                if (verbose) System.out.println("Un cliente esce dalla coda della stazione "+idStation+", pronto ad essere servito (Clienti in attesa "+waitingClients+"). Time: "+now()); //debug
                double serviceTime = d.nextSample();
                this.send(serviceTime,"departure");
                servingClients++;
                observer.updateServiceTime(serviceTime);
            }
        }
        acquaintances[0].send("arrival");
    }

}
