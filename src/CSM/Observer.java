package CSM;

public class Observer {
    private double totalSojournTime;
    private double serviceTime;
    private int departures;

    public Observer(){
        totalSojournTime=0;
        serviceTime=0;
        departures=0;
    }

    public void updateTotalSojournTime(double quantity){
        totalSojournTime+=quantity;
    }

    public void updateServiceTime(double quantity){
        serviceTime+=quantity;
    }

    public void incrementDeparture(){
        departures+=1;
    }

    public int getDepartures() { return departures; }

    public double getServiceTime() {return serviceTime; }


    public double sojournTime(){
        return totalSojournTime/departures;
    }

    public double throughput(double tEnd){
        return departures/tEnd;
    }

    public double utilization(double tEnd){
        return serviceTime/tEnd;
    }

}
