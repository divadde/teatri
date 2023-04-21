package CSM_Little_Law;

import smart.theatre.distributions.*;
import smart.theatre.standalone.ControlMachine;
import smart.theatre.standalone.Simulation;

import java.util.Random;

public class MainProva {

    public static void main(String[] args){

        Random r = new Random();

        //Creazione distribuzioni di probabilità
        Distribution ds0 = new ExponentialDistribution(r, Parameters.mu0);
        Distribution ds1 = new ExponentialDistribution(r, Parameters.mu1);
        Distribution ds2 = new ExponentialDistribution(r, Parameters.mu2);
        Distribution ds3 = new HyperExponentialDistribution(r, Parameters.a, Parameters.mu3);
        Distribution ds4 = new ErlangDistribution(r, Parameters.n4, Parameters.mu4);
        Distribution dr = new UniformDistribution(r,0,1);

        //Creazione ControlMachine
        ControlMachine cm = new Simulation(Parameters.tEnd);

        //Creazione stazioni
        AbstractStation s0 = new ReflectiveStation();
        AbstractStation s1 = new Station();
        AbstractStation s2 = new Station();
        AbstractStation s3 = new Station();
        AbstractStation s4 = new Station();
        AbstractStation router = new Router();

        //Creazione observers
        Observer os0 = new Observer(), os1 = new Observer(), os2 = new Observer(), os3 = new Observer(), os4 = new Observer();

        //Inizializzazione delle stazioni
        AbstractStation[] acqS0 = {s1}; //Acquaintances
        s0.send("init",ds0,acqS0,Parameters.numClients,os0,Parameters.verbose);

        AbstractStation[] acqS1 = {router}; //Acquaintances
        s1.send("init",ds1,acqS1,Parameters.numServersS1,1,os1,Parameters.verbose);

        AbstractStation[] acqR = {s0,s2,s3,s4}; //Acquaintances
        router.send("init",dr,acqR,Parameters.q,Parameters.delay);

        AbstractStation[] acqS234 = {s1}; //Acquaintances
        s2.send("init",ds2,acqS234,1,2,os2,Parameters.verbose);
        s3.send("init",ds3,acqS234,1,3,os3,Parameters.verbose);
        s4.send("init",ds4,acqS234,1,4,os4,Parameters.verbose);

        //Inizio simulazione
        cm.controller();

        //Risultati della simulazione //todo: problemi con il sojourn time
        System.out.println("\n Resume stazione 1:");
        System.out.println("Sojourn time: "+os1.sojournTime(Parameters.tEnd, Parameters.mu1));
        System.out.println("Throughput: "+os1.throughput(Parameters.tEnd));
        System.out.println("Utilization: "+os1.utilization(Parameters.tEnd));

        System.out.println("\n Resume stazione 2:");
        System.out.println("Sojourn time: "+os2.sojournTime(Parameters.tEnd, Parameters.mu2));
        System.out.println("Throughput: "+os2.throughput(Parameters.tEnd));
        System.out.println("Utilization: "+os2.utilization(Parameters.tEnd));

        System.out.println("\n Resume stazione 3:");
        System.out.println("Sojourn time: "+os3.sojournTime(Parameters.tEnd, Parameters.mu2)); //PLACE HOLDER todo mu iperesponenz
        System.out.println("Throughput: "+os3.throughput(Parameters.tEnd));
        System.out.println("Utilization: "+os3.utilization(Parameters.tEnd));

        System.out.println("\n Resume stazione 4:");
        System.out.println("Sojourn time: "+os4.sojournTime(Parameters.tEnd, Parameters.mu4)); //PLACE HOLDER todo mu erlang
        System.out.println("Throughput: "+os4.throughput(Parameters.tEnd));
        System.out.println("Utilization: "+os4.utilization(Parameters.tEnd));

        System.out.println("\n Resume intero sistema:");
        System.out.println("Sojourn time: "+os0.sojournTime(Parameters.tEnd, os0.throughput(Parameters.tEnd)));
        System.out.println("Throughput: "+os0.throughput(Parameters.tEnd));
        System.out.println("Utilization: "+os0.utilization(Parameters.tEnd));
    }

}
