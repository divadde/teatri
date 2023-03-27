package CSM;

public class Observer {

    //todo: verificare correttezza e se manca qualcosa
    private double busyTime;
    private double serviceTime;
    private double departures;

    public Observer(){
        busyTime=0;
        serviceTime=0;
        departures=0;
    }

    public void updateBusyTime(double quantity){
        busyTime+=quantity;
    }

    public void updateServiceTime(double quantity){
        serviceTime+=quantity;
    }

    public void incrementDeparture(){
        departures+=1;
    }

    public double sojournTime(){
        return busyTime/departures;
    }

    public double throughput(double tEnd){
        return departures/tEnd;
    }

    public double utilization(double tEnd){
        return serviceTime/tEnd;
    }

}
