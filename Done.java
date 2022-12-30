import java.util.function.Supplier;

class Done implements Event {
    private final Customer customer;
    private final Server server;
    private final double time;

    Done(Customer cust, Server server, double time) {
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
        Supplier<Double> restTimes) {
        int num = Math.min(server.getNum() - 1, s.size() - 1);
        return new Pair<Server, PQ<Event>>(s.get(num).rest(restTimes), pq);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d done serving by ", this.getTime(), 
            this.getNumber()) + this.server.toString() + "\n"; 
    }
}
