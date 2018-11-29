package domain;

public class SimulationSettings {
    private int minCustomerLoad;
    private int maxCustomerLoad;

    private double minProcessTime;
    private double maxProcessTime;

    private double minShopTime;
    private double maxShopTime;

    private double mean;
    private double variance;

    public SimulationSettings(int minCustomerLoad, int maxCustomerLoad,double customerSpawnMean,double customerSpawnvariance) {
        this.minCustomerLoad = minCustomerLoad;
        this.maxCustomerLoad = maxCustomerLoad;

        this.mean  = customerSpawnMean;
        this.variance = customerSpawnvariance ;

    }

    public synchronized double getMean() {
        return mean;
    }

    public synchronized double getVariance() {
        return variance;
    }


    public synchronized int getMinCustomerLoad() {
        return minCustomerLoad;
    }

    public synchronized int getMaxCustomerLoad() {
        return maxCustomerLoad;
    }

    public synchronized double getMinProcessTime() {
        return minProcessTime;
    }

    public synchronized double getMaxProcessTime() {
        return maxProcessTime;
    }

    public synchronized double getMinShopTime() {
        return minShopTime;
    }

    public synchronized double getMaxShopTime() {
        return maxShopTime;
    }

    @Override
    public String toString() {
        return "SimulationSettings{" +
                "minCustomerLoad=" + minCustomerLoad +
                ", maxCustomerLoad=" + maxCustomerLoad +
                ", minProcessTime=" + minProcessTime +
                ", maxProcessTime=" + maxProcessTime +
                ", minShopTime=" + minShopTime +
                ", maxShopTime=" + maxShopTime +
                '}';
    }

    public synchronized void setMinCustomerLoad(int minCustomerLoad) {
        this.minCustomerLoad = minCustomerLoad;
    }

    public synchronized void setMaxCustomerSettings(int maxCustomerLoad) {
        this.maxCustomerLoad = maxCustomerLoad;
    }

    public synchronized   void setCustomerSpawnMean(double customerSpawnMean) {
        this.mean = customerSpawnMean;
    }

    public synchronized void setCustomerSpawnVariance(double customerSpawnVariance) {
        this.variance = customerSpawnVariance;
    }
}
