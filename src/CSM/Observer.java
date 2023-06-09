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

    public void updateTotalSojournTime(double time){
        totalSojournTime+=time;
    }
    public void incrementDeparture(){
        departures+=1;
    }
    public void updateServiceTime(double time) {serviceTime+=time; }


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
