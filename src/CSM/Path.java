package CSM;

public class Path {

    public int n=0;
    private double A=0, t=0;

    public void up(double t){
        A += n*(t-this.t); n++; this.t=t;
    }

    public void down(double t){
        A += n*(t-this.t); n--; this.t=t;
    }

    public double mean(double tEnd){
        return A/tEnd;
    }

}
