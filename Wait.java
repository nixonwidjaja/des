import java.util.function.Supplier;

class Wait implements Event {
    private final Customer customer;
    private final Server server;

    Wait(Customer cust, Server server) {
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
        return s.add(0, 0, 0);
    }

    @Override
    public Pair<Server, PQ<Event>> process(ImList<Server> s, PQ<Event> pq,
        Supplier<Double> restTime) {
        int num = Math.min(server.getNum() - 1, s.size() - 1);
        pq = pq.add(new KeepWaiting(customer, s.get(num), s.get(num).getServeTime()));
        return new Pair<Server, PQ<Event>>(s.get(num), pq);  
    }

    @Override 
    public String toString() {
        return String.format("%.3f %d waits at ", this.getTime(), 
            this.getNumber()) + server.name() + "\n";
    }
}
