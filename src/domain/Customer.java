package domain;

import service.CheckoutController;

import java.util.Objects;

public class Customer   implements  Runnable{
    private int itemCount;
    private String name;
    private boolean waiting;
    private boolean leftStore = false;
    private int currentItemInBasket;

    private long startWaitingTime;
    private CheckoutController checkoutController;
    private SimulationStatistics simulationStatistics;

    public Customer(String name, int itemCount, CheckoutController checkoutController,TimeController timeController){
        this.name =name;
        this.waiting = false;
        this.itemCount= itemCount;
        this.checkoutController = checkoutController;
        simulationStatistics = SimulationStatistics.getInstance(timeController);
    }
    public synchronized void setStartWaitingTime(long startWaitingTime){
        this.startWaitingTime = startWaitingTime;
    }
    public synchronized long getStartWaitingTime(){
        return  this.startWaitingTime;
    }
    public synchronized int getItemCount(){
        return itemCount;
    }
    public synchronized String getName(){
        return  name;
    }
    public synchronized void addItemsInBasket(int i){
        simulationStatistics.customerPicksUpItem(i);
        currentItemInBasket += i;
    }
    public synchronized void stopWaiting(){
        waiting = false;
    }
    public synchronized void leftStore(){
        leftStore = true;
    }

    public synchronized boolean isWaiting(){
        return waiting;
    }
    @Override
    public void run() {
        //ADD customer to Stats
        simulationStatistics.customerEntersShop(getName());
        //should customer leave the store because it is to busy?
        int currentMinQueue = checkoutController.getMinQueueSize();
        if(currentMinQueue>10){
            double random = Math.random()*10;
            if(random<= currentMinQueue-10){
                System.out.println("Customer: " + getName() + " is leaving the store");
                simulationStatistics.customerLeavesStore(getName());
                return;
            }
        }

        //Loop to pickup Items
        while (currentItemInBasket<itemCount){
            try {
                // Customer is IDLE for min 1s to max 11s
                Thread.sleep((long) (Math.random()*10000+1000));
            } catch (InterruptedException e) {

            }
            System.out.println("Customer: "+ getName() +" Picking Up Item");
            int i = (int) Math.random()*3 +1;
            //To prevent going over the itemCount
            if(currentItemInBasket+ i>itemCount){
                i = itemCount-currentItemInBasket;
            }
            addItemsInBasket(i);
        }
        simulationStatistics.customerTotalItems(getName(),itemCount);
        System.out.println(this.getName() + " picked up all his items and is proceeding to checkout");
        boolean checkoutFound= false;
        Checkout checkout = null;
        while(!checkoutFound){
           checkout = checkoutController.determineCheckout();
           System.out.println(this.getName() +"Looking for checkout");
            if(checkout!=null){
                checkoutFound = true;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.getName() +" found checkout:  "+checkout.getName());
        waiting = true;
        leftStore = false;
        checkout.addCustomer(this);
        double chanceCustomerchangeCheckout = 0.5;
        while(waiting){
            Checkout checkoutTemp = checkoutController.isBetterCheckoutAvailable(checkout);
            if(checkoutTemp != null){
                int[] currentPosition = checkout.getCustomerPosition(getName());
                if(currentPosition != null ){
                    // if queue is longer then 4
                    if(checkout.getQueueSize()>4){
                        // if you are in the last or second last position of the line + 50%
                        if( currentPosition[1]-checkoutTemp.getQueueSize()>=2 &&Math.random()>chanceCustomerchangeCheckout){
                            chanceCustomerchangeCheckout += 0.2;
                            boolean isRemoved =  checkout.removeCustomer(getName());
                            if(isRemoved){
                                checkoutTemp.addCustomerChangedCheckout(this);
                                simulationStatistics.customerChangedCheckout(getName());
                                System.out.println("|||||||||||||||||||||CUSTOMER ADDED to different QUEUE||||||||||||||||||||||||||||||");
                            }
                        }
                    }
                }
            }
            System.out.println("Customer: " + getName() +"  is waiting at Checkout: "+ checkout.getName());
            try{
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(!leftStore){
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("*****************************Thread Finished***************************");

    }

}
