import java.util.function.Supplier;

class Leave implements Event {
    private final Customer customer;
    private final Server server;

    Leave(Customer cust, Server server) {
        this.customer = cust;
        this.server = server;
    }

    @Override
    public double getTime() {
        return this.customer.getArriveTime();
    }

    @Override
    public int getNumber() {
        return this.customer.getNum();
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public Stats add(Stats s) {
        return s.add(0, 1, 0);
    }
    
    @Override
    public Pair<Server, PQ<Event>> process(ImList<Server> s, PQ<Event> pq,
        Supplier<Double> restTime) {
        return new Pair<Server, PQ<Event>>(s.get(0), pq);  
    }

    @Override 
    public String toString() {
        return String.format("%.3f %d leaves\n", this.getTime(), this.getNumber());
    }
}
