import java.util.function.Supplier;

class Server {
    private final int num;
    private final int qmax;
    private final ImList<Customer> waiting;
    private final Customer currCustomer;
    private static final double epsilon = 1e-10;
    private final double serveTime;
    
    Server(int num, int qmax) {
        this.num = num;
        this.qmax = qmax;
        this.waiting = new ImList<Customer>();
        this.currCustomer = new Customer(0, 0.0, Lazy.of(() -> 0.0));
        this.serveTime = 0;
    }

    Server(int num, int qmax, Customer c, double serveTime) {
        this.num = num;
        this.qmax = qmax;
        this.waiting = new ImList<Customer>();
        this.currCustomer = c;
        this.serveTime = serveTime;
    }

    Server(int num, int qmax, ImList<Customer> waiting, Customer customer, double serveTime) {
        this.num = num;
        this.qmax = qmax;
        this.waiting = waiting;
        this.currCustomer = customer;
        this.serveTime = serveTime;
    }

    Server(Server s) {
        this.num = s.num;
        this.qmax = s.qmax;
        this.waiting = s.waiting;
        this.currCustomer = s.currCustomer;
        this.serveTime = s.serveTime;
    }

    int getNum() {
        return this.num;
    }

    int getQmax() {
        return qmax;
    }

    ImList<Customer> getWaiting() {
        return waiting;
    }

    Customer nextCust() {
        return this.waiting.get(0);
    }

    double getServeTime() {
        return this.serveTime;
    } 

    boolean isAvailable(Customer newCustomer) {
        return newCustomer.getArriveTime() + epsilon >= this.getServeTime();
    }

    boolean isAvailable(double time) {
        return time + epsilon >= this.getServeTime();
    }

    Server rest(Supplier<Double> restTime) {
        return new Server(num, qmax, waiting, currCustomer, serveTime + restTime.get());
    }

    Server serve(Customer c) {
        if (this.isAvailable(c)) {
            return new Server(num, qmax, c, c.getArriveTime() + c.getDuration());
        }
        return this;
    }

    boolean canWait() {
        return this.waiting.size() < qmax;
    }
    
    boolean hasNext() {
        return this.waiting.size() > 0;
    }
    
    Server wait(Customer c) {
        if (this.canWait()) {
            ImList<Customer> list = waiting.add(c);
            return new Server(num, qmax, list, currCustomer, this.getServeTime());
        }
        return this;
    }
    
    Server update() {
        if (this.waiting.size() > 0) {
            Customer c = waiting.get(0);
            ImList<Customer> list = waiting.remove(0);
            return new Server(num, qmax, list, c, this.getServeTime() + c.getDuration());
        }
        return this;
    }
    
    Server update(Customer c) {
        return new Server(num, qmax, c, this.getServeTime() + c.getDuration());
    }

    @Override
    public String toString() {
        return String.format("%d", num);
    }

    public String name() {
        return String.format("%d", num); 
    }
}
