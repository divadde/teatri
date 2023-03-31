package CSM;

public class Client {
    //forse si può cacciare la classe Client, grazie alla Legge di Little //todo: probabilmente il Client ci serve comunque
    private int id;
    private double startWaitingTime;
    private double departureTime;

    private double globalArrivalTime;
    private double globalDepartureTime;

    public Client(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public void setStartWaitingTime(double startWaitingTime) {
        this.startWaitingTime = startWaitingTime;
    }
    public void setDepartureTime(double departureTime){
        this.departureTime = departureTime;
    }

    public double getStartWaitingTime(){
        return startWaitingTime;
    }
    public double getDepartureTime(){
        return departureTime;
    }



    public void setGlobalArrivalTime(double globalArrivalTime) {
        this.globalArrivalTime = globalArrivalTime;
    }
    public void setGlobalDepartureTime(double globalDepartureTime){
        this.globalDepartureTime = globalDepartureTime;
    }
    public double getGlobalArrivalTime(){
        return globalArrivalTime;
    }
    public double getGlobalDepartureTime(){
        return globalDepartureTime;
    }

}
