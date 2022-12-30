class Stats {
    private final int served;
    private final int left;
    private final double waitTime;

    Stats(int s, int l, double w) {
        this.served = s;
        this.left = l;
        this.waitTime = w;
    }

    Stats add(int s, int l, double w) {
        return new Stats(served + s, left + l, waitTime + w);
    }

    @Override
    public String toString() {
        return String.format("[%.3f %d %d]", waitTime / served, served, left);
    }
}
