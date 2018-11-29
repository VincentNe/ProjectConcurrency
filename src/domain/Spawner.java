package domain;

import service.CheckoutController;
import service.ShopController;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Spawner implements  Runnable {
    private  boolean isOpen = true;
    private TimeController timeController;

    private long lastSpawn;

    private SimulationSettings settings;
    private ThreadGroup threadGroup;
    private CheckoutController checkoutController;
    private SimulationStatistics statistics;
    private ShopController shopController;
    //ONLY USED FOR LABELING
    private volatile int customercount;

    public Spawner(TimeController timeController, SimulationSettings settings, CheckoutController checkoutController,SimulationStatistics statistics,ShopController shopController){
        this.timeController = timeController;
        this.settings = settings;
        this.checkoutController = checkoutController;
        this.statistics = statistics;
        this.shopController = shopController;
        threadGroup = new ThreadGroup("Customers");
        //TODO PROBLEM WHEN MEAN IS REALLY SMALL
    }


    private void spawnCustomersOverTime(int spawnCount, double overTime){
        for(int i = 0; i< spawnCount;i++){
            long spawnDelay = (long) (Math.random()*overTime);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // Calculate item Count Based on the User input
                   spawnCustomer();
                }
            }, spawnDelay);
        }
    }
    private synchronized void spawnCustomer(){
        if(isOpen==false) {
            return;
        }
        int itemcount = new Random().nextInt((settings.getMaxCustomerLoad() - settings.getMinCustomerLoad()) + 1) + settings.getMinCustomerLoad();
        customercount++;
        new Thread(threadGroup, new Customer("Customer " + customercount, itemcount, checkoutController,timeController)).start();
        System.out.println("Customer: " + customercount + " is spawned");
    }
    public void spawnBulk(int customerCount){
        System.out.println("BulkSpawn " + +customerCount +" customers");
        (new Thread() {
            public void run() {
                for (int i=0;i<customerCount;i++) {
                    spawnCustomer();
                }
            }
        }).start();
    }
    public void stopSpawning(){
        isOpen = false;
        System.out.println("Stop Spawning new Customers");
        //Checks When shop is empty
        (new Thread() {
            public void run() {
                Thread[] threads = new Thread[threadGroup.activeCount()];
                threadGroup.enumerate(threads);
                while(threadGroup.activeCount()>0){
                    threadGroup.enumerate(threads);
                    try{
                        threads[0].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                shopEmpty();

            }
        }).start();
    }
    public void shopEmpty(){
        shopController.shopIsFinished();
    }
    @Override
    public void run() {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        //initialize first customers
        spawnCustomersOverTime((int) settings.getMean(), 60000);

        while(isOpen){
            // Stored current Simulation Time
            long tempCurrentTime  = timeController.getRunningTimeInMiliseconds();
            // Calculates Time since last spawn
            double timePassed = (double) tempCurrentTime -lastSpawn;

            if(timePassed> 60000){
                //Store current time
                lastSpawn = tempCurrentTime;
                System.out.println("StartSpawn: "+ settings.getMean());
                // Random number based on standard deviation
                double random = settings.getMean() + generator.nextGaussian() * settings.getVariance();
                double timePassedInMinutes =  (timePassed/1000/60);
                // Calculate spawnCount Based on time passed in minutes
                int spawnCount = (int) Math.round(random * timePassedInMinutes);

                spawnCustomersOverTime(spawnCount, timePassed);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
