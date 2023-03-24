import smart.theatre.distributions.Distribution;
import smart.theatre.distributions.ExponentialDistribution;
import smart.theatre.standalone.Actor;
import smart.theatre.standalone.ControlMachine;
import smart.theatre.standalone.Simulation;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Teatro t = new Teatro();
        ControlMachine c = new Simulation(10000);
        t.send(0,"init",new ExponentialDistribution(new Random(),2));
        c.controller();
    }
}


class Teatro extends Actor {
    Distribution d;
    @Msgsrv
    public void init(Distribution d){
        this.d=d;
        send(this.d.nextSample(),"init",this.d);
        System.out.println("teatro attivo");
    }
}