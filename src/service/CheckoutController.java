package service;

import domain.Checkout;
import domain.Customer;
import domain.TimeController;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutController {
    //Holds all the checkouts
    HashMap<String,Checkout> checkouts;
    //same as isSimulationrunning
    private boolean checkoutsRunning;

    public CheckoutController(){
        checkouts = new HashMap<>();
        checkoutsRunning = false;
    }

    public int getMinQueueSize() {
        int checkoutQueueSize = Integer.MAX_VALUE;
        synchronized(checkouts) {
            for (Checkout checkout : checkouts.values()) {
                if(!checkout.isClosed()) {
                    if (checkout.getQueueSize() <= checkoutQueueSize) {
                        checkoutQueueSize = checkout.getQueueSize();
                    }
                }
            }
        }
        return checkoutQueueSize;
    }


    public void addCheckout(String UID,Checkout checkout){
        if(checkout == null) throw new NullPointerException("Can't add a null checkout");
        if(checkoutsRunning){
            Thread thread = new Thread(checkout);
            thread.start();
        }

        checkouts.put(UID,checkout);
    }

    public Checkout determineCheckout(){
        Checkout tempC= null;
        int checkoutQueueSize = Integer.MAX_VALUE;
        synchronized(checkouts) {
            for (Checkout checkout : checkouts.values()) {
                if(!checkout.isClosed()) {
                    if (checkout.getQueueSize() <= checkoutQueueSize) {
                        tempC = checkout;
                        checkoutQueueSize = checkout.getQueueSize();
                    }
                }
            }
        }
        return tempC;
    }

    public void startCheckouts() {
        checkoutsRunning = true;
        for (Checkout checkout:checkouts.values()){
            Thread thread = new Thread(checkout);
            thread.start();
        }
    }

    public synchronized void openCheckout(String checkoutName) {
        checkouts.get(checkoutName).openCheckout();
    }

    public synchronized void closeCheckout(String checkoutName) {
        checkouts.get(checkoutName).closeCheckout();
    }

    public boolean IsCheckoutNameAvailable(String uid) {
        return !checkouts.containsKey(uid);
    }

    public Checkout isBetterCheckoutAvailable(Checkout checkout) {
        Checkout otherOption = determineCheckout();

        if(checkout.getName().equals(otherOption.getName()))
        return  null;

        return otherOption;
    }

    public void stopCheckouts() {
        for (Checkout c: checkouts.values()) {
            c.stopRunning();
        }
    }
}
