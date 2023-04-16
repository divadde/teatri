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

    private AbstractStation getRandomStation(){ //Metodo specifico per 4 stazioni di riferimento
        int position=0;
        double extracted = d.nextSample();
        double soglia1 = probabilities[0];
        double soglia2 = probabilities[1]+soglia1;
        double soglia3 = probabilities[2]+soglia2;
        if (extracted<=soglia1) position=0;
        if (soglia1 < extracted && extracted <= soglia2) position=1;
        if (soglia2 < extracted && extracted <= soglia3) position=2;
        if (soglia3 < extracted) position=3;
        return acquaintances[position];
    }

    private double getDelay(){
        return d.nextSample()*(delayRange[1]-delayRange[0])+delayRange[0];
    }

}
