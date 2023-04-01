package CSM;

import smart.theatre.distributions.Distribution;

import java.util.Arrays;

public class Router extends AbstractStation{

    private Double[] probabilities;
    private Double[] delayRange;

    @Msgsrv
    public void init(Distribution d, AbstractStation[] acquaintances, Double[] probabilities, Double[] delayRange) throws IllegalArgumentException {
        if (acquaintances.length==0 || probabilities.length!=acquaintances.length || delayRange.length!=2) throw new IllegalArgumentException();
        super.send("init",d,acquaintances);
        this.probabilities=probabilities;
        this.delayRange=delayRange;
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        this.send(getDelay(),"departure",c);
    }

    @Override @Msgsrv
    public void departure(Client c) {
        AbstractStation station = getRandomStation();
        station.send("arrival",c);
    }

    //todo: rendere più generico?
    private AbstractStation getRandomStation(){
        int position=0;
        double extracted = d.nextSample();
        double intervallo1 = probabilities[0];
        double intervallo2 = probabilities[1]+intervallo1;
        double intervallo3 = probabilities[2]+intervallo2;
        if (extracted<=intervallo1) position=0;
        if (intervallo1 < extracted && extracted <= intervallo2) position=1;
        if (intervallo2 < extracted && extracted <= intervallo3) position=2;
        if (intervallo3 < extracted) position=3;
        return acquaintances[position];
    }

    private double getDelay(){
        return d.nextSample()*(delayRange[1]-delayRange[0])+delayRange[0];
    }

}
