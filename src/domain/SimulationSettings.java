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

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }


    public int getMinCustomerLoad() {
        return minCustomerLoad;
    }

    public int getMaxCustomerLoad() {
        return maxCustomerLoad;
    }

    public double getMinProcessTime() {
        return minProcessTime;
    }

    public double getMaxProcessTime() {
        return maxProcessTime;
    }

    public double getMinShopTime() {
        return minShopTime;
    }

    public double getMaxShopTime() {
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

}
