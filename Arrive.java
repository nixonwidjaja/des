import java.util.function.Supplier;

class Arrive implements Event {
    private final Customer customer;

    Arrive(Customer cust) {
        this.customer = cust;
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
        return new Server(0, 0);
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
        Event e;
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).isAvailable(customer)) {
                Server server = s.get(i).serve(customer);
                e = new Serve(customer, server, customer.getArriveTime());
                return new Pair<Server, PQ<Event>>(server, pq.add(e));
            }
        }
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).canWait()) {
                e = new Wait(customer, s.get(i).wait(customer));
                return new Pair<Server, PQ<Event>>(s.get(i).wait(customer), pq.add(e)); 
            }
        }
        e = new Leave(customer, s.get(0));
        return new Pair<Server, PQ<Event>>(s.get(0), pq.add(e));  
    }

    @Override
    public String toString() {
        return String.format("%.3f %d arrives\n", this.getTime(), this.getNumber());
    }
}
