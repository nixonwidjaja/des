import java.util.function.Supplier;

class KeepWaiting implements Event {
    private final Customer customer;
    private final Server server;
    private final double time;

    KeepWaiting(Customer cust, Server server, double time) {
        this.customer = cust;
        this.server = server;
        this.time = time;
    }

    @Override
    public double getTime() {
        return this.time;
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
        Server server = s.get(num);
        if (server.isAvailable(time)) {
            Server updated = server.update();
            pq = pq.add(new Serve(customer, updated, server.getServeTime()));
            return new Pair<Server, PQ<Event>>(updated, pq); 
        }
        pq = pq.add(new KeepWaiting(customer, server, server.getServeTime()));
        return new Pair<Server, PQ<Event>>(server, pq);  
    }

    @Override 
    public String toString() {
        return "";
    }
}
