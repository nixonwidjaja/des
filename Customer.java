class Customer {
    private final int num;
    private final double arriveTime;
    private final Lazy<Double> duration;

    Customer(int num, double arriveTime, Lazy<Double> duration) {
        this.num = num;
        this.arriveTime = arriveTime;
        this.duration = duration;
    }

    int getNum() {
        return this.num;
    }
    
    double getArriveTime() {
        return this.arriveTime;
    }

    double getDuration() {
        return this.duration.get();
    }
}
