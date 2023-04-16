package CSM;

public class Client {
    private int id;
    private double arrivalTime; //Istante di arrivo in una stazione generica
    private double globalArrivalTime; //Istante di arrivo nel sistema
    private double startService; //Istante in cui il cliente inizia un servizio

    public Client(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public void setArrivalTime(double startWaitingTime) {
        this.arrivalTime = startWaitingTime;
    }
    public double getArrivalTime(){
        return arrivalTime;
    }

    public void setGlobalArrivalTime(double globalArrivalTime) {
        this.globalArrivalTime = globalArrivalTime;
    }
    public double getGlobalArrivalTime(){
        return globalArrivalTime;
    }

    public void setStartService(double startService) {
        this.startService = startService;
    }
    public double getStartService() {
        return startService;
    }


}
