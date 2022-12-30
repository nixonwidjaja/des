import java.util.function.Supplier;

class Simulator {
    private final int numServer;
    private final int numOfSelfChecks;
    private final int qmax;
    private final ImList<Pair<Double, Supplier<Double>>> input;
    private final Supplier<Double> restTimes;

    Simulator(int numServer, int numOfSelfChecks, int qmax, 
        ImList<Pair<Double, Supplier<Double>>> input, Supplier<Double> restTimes) {
        this.numServer = numServer;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qmax = qmax;
        this.input = input;
        this.restTimes = restTimes;
    }

    String simulate() {
        PQ<Event> pq = new PQ<Event>(new EventCmp());
        ImList<Server> s = new ImList<Server>();
        for (int i = 1; i <= numServer; i++) {
            s = s.add(new Server(i, qmax));
        }
        if (numOfSelfChecks > 0) {
            s = s.add(new SelfCheck(numServer + 1, qmax, numOfSelfChecks, numServer + 1));
        }
        String ans = "";
        Server server;
        Stats stats = new Stats(0, 0, 0);
        for (int i = 0; i < input.size(); i++) {
            Customer c = new Customer(i + 1, input.get(i).first(), Lazy.of(input.get(i).second()));
            pq = pq.add(new Arrive(c));
        }
        while (!pq.isEmpty()) {
            Pair<Event, PQ<Event>> pr = pq.poll();
            Event event = pr.first();
            pr = pq.poll();
            event = pr.first();
            pq = pr.second();
            stats = event.add(stats);
            ans += event.toString();
            Pair<Server, PQ<Event>> pair = event.process(s, pq, restTimes);
            server = pair.first();
            pq = pair.second();
            s = s.set(Math.min(server.getNum() - 1, numServer), server);
        }
        ans += stats.toString();
        return ans;
    }
}
