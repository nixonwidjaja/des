import java.util.function.Supplier;

interface Event {
    public Pair<Server, PQ<Event>> process(ImList<Server> s, PQ<Event> pq,
        Supplier<Double> restTime);

    public double getTime();

    public int getNumber();

    public Server getServer();

    public Customer getCustomer();

    public Stats add(Stats s);
}
