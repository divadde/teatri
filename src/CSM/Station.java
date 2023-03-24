package CSM;

import smart.theatre.distributions.Distribution;

import java.util.LinkedList;

public class Station extends AbstractStation{
    private int numServers;
    private int clientsInWaiting;
    private int servingClients;

    private int idStation;
    private LinkedList<Client> waitingLine = new LinkedList<>();

    //todo: forse non c'è bisogno di passare un array di abstractstation, tanto queste sono stazioni pensate per inviare a una sola stazione
    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Integer numServers, Integer idStation) throws IllegalArgumentException {
        if (acquaintances.length==0 || numServers<1) throw new IllegalArgumentException();
        super.send("init",d,acquaintances); //todo: verifica se funziona
        this.numServers=numServers;
        this.idStation = idStation;
        clientsInWaiting=0;
        servingClients=0;
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        System.out.println("Cliente "+c.getId()+" arrivato alla stazione "+idStation+". Time: "+now()); //debug
        switch(state){
            case FREE:
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
        System.out.println("Cliente "+c.getId()+" parte dalla stazione "+idStation+". Time: "+now()); //debug
        servingClients--;
        acquaintances[0].send("arrival",c);
        if (state==State.BUSY){
            if (waitingLine.size()==0) state=State.FREE;
            else {
                Client nextClient = waitingLine.removeFirst();
                clientsInWaiting--;
                System.out.println("Cliente "+nextClient.getId()+" esce dalla coda della stazione "+idStation+", pronto ad essere servito (Clienti in attesa "+clientsInWaiting+"). Time: "+now()); //debug
                this.send(d.nextSample(),"departure",nextClient);
                servingClients++;
            }
        }
    }

}
