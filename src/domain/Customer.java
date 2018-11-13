package domain;

import service.CheckoutController;

public class Customer  extends  Person implements  Runnable{
    private int itemCount;


    private int currentItemInBasket;

    private long startWaitingTime;
    private CheckoutController checkoutController;
    private SimulationStatistics simulationStatistics;

    public Customer(String name, int itemCount, CheckoutController checkoutController){
        super(name);
        this.itemCount= itemCount;
        this.checkoutController = checkoutController;
        simulationStatistics = SimulationStatistics.getInstance();
    }
    public void setStartWaitingTime(long startWaitingTime){
        this.startWaitingTime = startWaitingTime;
    }
    public long getStartWaitingTime(){
        return  this.startWaitingTime;
    }
    public int getItemCount(){
        return itemCount;
    }

    public void addItemsInBasket(int i){
        simulationStatistics.customerPicksUpItem(i);
        currentItemInBasket += i;
    }
    @Override
    public void run() {
        //ADD customer to Stats
        simulationStatistics.customerEntersShop();

        //Loop to pickup Items
        while (currentItemInBasket<itemCount){
            System.out.println("Customer: "+ getName() +" Picking Up Item");
            int i = (int) Math.random()*3 +1;
            //To prevent going over the itemCount
            if(currentItemInBasket+ i>itemCount){
                i = itemCount-currentItemInBasket;
            }
            addItemsInBasket(i);

            try {
                // Customer is IDLE for min 1s to max 11s
                Thread.sleep((long) (Math.random()*10000+1000));
            } catch (InterruptedException e) {

            }

        }
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
        checkout.addCustomer(this);

        //TODO SHOULD I Terminate This Thread ?
    }
}
