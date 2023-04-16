package CSM;

public class Client {
    //forse si può cacciare la classe Client, grazie alla Legge di Little //todo: probabilmente il Client ci serve comunque
    private int id;
    private double arrivalTime; //Istante di arrivo in una stazione generica
    private double departureTime; //Istante di uscita da una stazione generica

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
    public void setDepartureTime(double departureTime){
        this.departureTime = departureTime;
    }

    public double getArrivalTime(){
        return arrivalTime;
    }
    public double getDepartureTime(){
        return departureTime;
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
