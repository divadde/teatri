package CSM;

public class Observer {
    private double totalSojournTime;
    private double serviceTime;
    private double startService;
    private int departures;

    public Observer(){
        totalSojournTime=0;
        serviceTime=0;
        startService=0;
        departures=0;
    }

    public void updateTotalSojournTime(double quantity){
        totalSojournTime+=quantity;
    }
    public void startService(double time) { startService=time; }
    public void endService(double time){
        serviceTime+=time-startService;
    }
    public void incrementDeparture(){
        departures+=1;
    }


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
