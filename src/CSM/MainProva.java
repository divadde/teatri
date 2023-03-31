package CSM;

import smart.theatre.distributions.*;
import smart.theatre.standalone.ControlMachine;
import smart.theatre.standalone.Simulation;

import java.util.Random;

public class MainProva {

    public static void main(String[] args){
        Random r = new Random();

        double mu0 = 0.2;
        double mu1 = 0.5;
        double mu2 = 0.8;
        double[] mu3 = {0.4,0.1};
        double[] a = {0.95,0.05};
        int n4 = 2;
        double mu4 = 0.9;
        Double[] q = {0.2,0.3,0.3,0.2};
        Double[] delay = {0.5,1.5};

        int numClients = 2;
        int numServersS1 = 3; //pare che il grado di parallelismo consigliato sia 3.

        double tEnd = 36000;

        Distribution ds0 = new ExponentialDistribution(r,mu0);
        Distribution ds1 = new ExponentialDistribution(r,mu1);
        Distribution ds2 = new ExponentialDistribution(r,mu2);
        Distribution ds3 = new HyperExponentialDistribution(r,a,mu3);
        Distribution ds4 = new ErlangDistribution(r,n4,mu4);
        Distribution dr = new UniformDistribution(r,0,1);

        ControlMachine cm = new Simulation(tEnd);

        AbstractStation s0 = new ReflectiveStation();
        AbstractStation s1 = new Station();
        AbstractStation s2 = new Station();
        AbstractStation s3 = new Station();
        AbstractStation s4 = new Station();
        AbstractStation router = new Router();

        Observer os0 = new Observer();
        Observer os1 = new Observer(); Observer os2 = new Observer(); Observer os3 = new Observer(); Observer os4 = new Observer();

        AbstractStation[] acqS0 = {s1};
        s0.send("init",ds0,acqS0,numClients,os0);

        Path path = new Path();
        AbstractStation[] acqS1 = {router};
        s1.send("init",ds1,acqS1,numServersS1,1,os1,tEnd);
        s1.send("setPath",path);

        AbstractStation[] acqR = {s0,s2,s3,s4};
        router.send("init",dr,acqR,q,delay);

        AbstractStation[] acqS234 = {s1};
        s2.send("init",ds2,acqS234,1,2,os2,tEnd);
        s3.send("init",ds3,acqS234,1,3,os3,tEnd);
        s4.send("init",ds4,acqS234,1,4,os4,tEnd);


        cm.controller();

        System.out.println("\n Resume stazione 1:");
        System.out.println("Sojourn time: "+os1.sojournTime());
        System.out.println("Throughput: "+os1.throughput(tEnd));
        System.out.println("Utilization: "+os1.utilization(tEnd));
        System.out.println("Mean number of clients in service: "+path.mean(tEnd));

        System.out.println("\n Resume stazione 2:");
        System.out.println("Sojourn time: "+os2.sojournTime());
        System.out.println("Throughput: "+os2.throughput(tEnd));
        System.out.println("Utilization: "+os2.utilization(tEnd));

        System.out.println("\n Resume stazione 3:");
        System.out.println("Sojourn time: "+os3.sojournTime());
        System.out.println("Throughput: "+os3.throughput(tEnd));
        System.out.println("Utilization: "+os3.utilization(tEnd));

        System.out.println("\n Resume stazione 4:");
        System.out.println("Sojourn time: "+os4.sojournTime());
        System.out.println("Throughput: "+os4.throughput(tEnd));
        System.out.println("Utilization: "+os4.utilization(tEnd));

        System.out.println("\n Resume intero sistema:");
        System.out.println("Sojourn time: "+os0.sojournTime());
        System.out.println("Throughput: "+os0.throughput(tEnd));
        System.out.println("Utilization: "+os0.utilization(tEnd));
    }

}
