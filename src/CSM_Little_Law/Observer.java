package CSM_Little_Law;

public class Observer {
    private double serviceTime;
    private int departures;
    private Path path;

    public Observer() {
        serviceTime=0;
        departures=0;
    }

    public void incrementDeparture(){
        departures+=1;
    }
    public void updateServiceTime(double time) {serviceTime+=time; }
    public void setPath(Path path) {this.path=path;}


    public double sojournTime(double tEnd, double lambda){
        return path.mean(tEnd)/lambda;
    }

    public double throughput(double tEnd){
        return departures/tEnd;
    }

    public double utilization(double tEnd){
        return serviceTime/tEnd;
    }

}
