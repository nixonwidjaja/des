import java.util.function.Supplier;

class SelfCheck extends Server {
    private final ImList<Server> servers;
    private final int mainNum;
    private static final double epsilon = 1e-10;

    SelfCheck(int num, int qmax, int numOfSelfChecks, int mainNum) {
        super(num, qmax);
        this.mainNum = mainNum;
        ImList<Server> temp = new ImList<Server>();
        for (int i = 0; i < numOfSelfChecks; i++) {
            temp = temp.add(new Server(num + i, qmax));
        }
        this.servers = temp;
    }

    SelfCheck(int num, int qmax, ImList<Customer> waiting, Customer customer, 
        double serveTime, ImList<Server> servers, int mainNum) {
        super(num, qmax, waiting, customer, serveTime);
        this.mainNum = mainNum;
        this.servers = servers;
    }

    @Override
    public String toString() {
        return "self-check " + super.toString();
    }

    @Override
    SelfCheck rest(Supplier<Double> restTime) {
        return this;
    }

    @Override
    boolean isAvailable(Customer newCustomer) {
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isAvailable(newCustomer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isAvailable(double time) {
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isAvailable(time)) {
                return true;
            }
        }
        return false;
    }

    @Override
    SelfCheck serve(Customer c) {
        ImList<Server> news = servers;
        int idx = 0;
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isAvailable(c)) {
                news = news.set(i, servers.get(i).serve(c));
                idx = servers.get(i).getNum();
                break;
            }
        }
        double minTime = news.get(0).getServeTime();
        for (int i = 0; i < news.size(); i++) {
            if (news.get(i).getServeTime() < minTime) {
                minTime = news.get(i).getServeTime();
            }
        }
        return new SelfCheck(idx, getQmax(), new ImList<Customer>(), c, minTime, news, mainNum);
    }

    @Override
    SelfCheck wait(Customer c) {
        if (this.canWait()) {
            ImList<Customer> list = getWaiting().add(c);
            return new SelfCheck(mainNum, getQmax(), list, c, getServeTime(), servers, mainNum);
        }
        return this;
    }

    @Override
    SelfCheck update() {
        if (getWaiting().size() > 0) {
            ImList<Server> news = servers;
            Customer c = getWaiting().get(0);
            ImList<Customer> list = getWaiting().remove(0);
            int idx = 0;
            for (int i = 0; i < servers.size(); i++) {
                if (servers.get(i).getServeTime() - epsilon <= getServeTime()) {
                    news = news.set(i, servers.get(i).update(c));
                    idx = servers.get(i).getNum();
                    break;
                }
            }
            double minTime = news.get(0).getServeTime();
            for (int i = 0; i < news.size(); i++) {
                if (news.get(i).getServeTime() < minTime) {
                    minTime = news.get(i).getServeTime();
                }
            }
            return new SelfCheck(idx, getQmax(), list, c, minTime, news, mainNum);
        }
        return this;
    }

    public String name() {
        return "self-check " + mainNum;
    }
}
