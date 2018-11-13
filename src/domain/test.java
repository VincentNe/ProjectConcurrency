package domain;

import java.util.concurrent.ThreadLocalRandom;

public class test {

    public static  void main(String[] args){
        test te = new test();
        te.gaussian(6,2);
    }

    void gaussian(double mean, double variance) {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        double te = 0;
        double CountNotSpawned=0;
        for (int idx = 1; idx <= 1000; ++idx) {
            double random = mean + generator.nextGaussian() * variance;
            if((random/12)<0.5)CountNotSpawned++;
            te+= (random/12);
        }
        System.out.println(te/1000);
        System.out.println(CountNotSpawned);
    }
}
