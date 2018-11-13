package domain;


import service.ShopController;

import java.util.ArrayDeque;
import java.util.Queue;

public class CheckoutImpl implements Checkout {
    private Scanner scanner;
    private Employe employe;
    private Queue<Customer> customerQueue;
    private boolean isRunning;
    private boolean isClosed;
    private int maxItems;
    private int lowestCustomerThroughput;
    private int highestCustomerThroughPut;
    private String checkoutLabel;

    private TimeController timeController;
    private SimulationStatistics simulationStatistics;

    public  CheckoutImpl(int maxItems,int scanDelay ,int lowestCustomerThroughPut, int highestCustomerThroughPut, String checkoutLabel, TimeController timeController, SimulationStatistics simulationStatistics){
        this.simulationStatistics = simulationStatistics;
        this.timeController = timeController;
        this.checkoutLabel  = checkoutLabel;
        isRunning = true;
        isClosed= true;
        employe = new Employe("ERIC");
        scanner = new ScannerImpl(scanDelay);

        customerQueue = new ArrayDeque<>();
        this.maxItems= maxItems;
        this.lowestCustomerThroughput = lowestCustomerThroughPut;
        this.highestCustomerThroughPut = highestCustomerThroughPut;
    }
    public String getName(){
        return checkoutLabel;
    }

    public synchronized void addCustomer(Customer customer){
        simulationStatistics.customerToCheckout();
        customer.setStartWaitingTime(timeController.getRunningTimeInSeconds());
        customerQueue.add(customer);
        // Notify Controller
        ShopController.shopControllerInstance().updateCheckoutQueueSizeUI(checkoutLabel,customerQueue.size());
    }
    private synchronized Customer getFirstinQueueCustomer(){
        return customerQueue.remove();
    }
    public int getQueueSize(){
        return customerQueue.size();
    }

    @Override
    public void scanItem() {

    }

    public void closeCheckout(){
        isClosed = true;
    }
    public void openCheckout(){isClosed = false;}
    public boolean isClosed(){return  isClosed;}

    private long checkoutOpenTime = -1;
    private long totalNotUtilizedTime;
    private long lastUpdateTotalNotUtilizedTime;

    private long totalOpenTime;

    private boolean isUtilized = false;
    @Override
    public void run() {
        if(checkoutOpenTime==-1){
            checkoutOpenTime = timeController.getRunningTimeInMiliseconds();
            lastUpdateTotalNotUtilizedTime = checkoutOpenTime;
        }
        boolean updateStatistics = true;
        while(isRunning){
            /*
            if(isClosed && getQueueSize()<=0){
                updateStatistics = false;

            }else{
                updateStatistics= true;
               lastUpdateTotalNotUtilizedTime = timeController.getRunningTimeInMiliseconds();
            }
            */
            if( getQueueSize()>0){
                if(!isUtilized){
                    isUtilized = true;
                    totalNotUtilizedTime+= timeController.getRunningTimeInMiliseconds() - lastUpdateTotalNotUtilizedTime;
                }
                Customer c = getFirstinQueueCustomer();

                ShopController.shopControllerInstance().updateCheckoutQueueSizeUI(checkoutLabel,customerQueue.size());
                for (int i = 0; i < c.getItemCount(); i++) {
                    System.out.println("Checkout: " +getName() +" scanned item from Customer: " + c.getName());
                    //update process bar
                    ShopController.shopControllerInstance().updateCheckoutScanningProcess(checkoutLabel, i+1,c.getItemCount());
                    try {
                        //TODO BASE SCAN SPEED ON EMPLOYE
                        long scanDelay = (new Double(Math.random()*4000)).longValue() + scanner.getScanDelay();
                        Thread.sleep(scanDelay);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //reset process bar
                ShopController.shopControllerInstance().updateCheckoutScanningProcess(checkoutLabel, 0,10);

                System.out.println("Checkout: "+ getName() + " finished with customer: " + c.getName());

                ShopController.shopControllerInstance().addCustomerStatistics(timeController.getRunningTimeInSeconds() - c.getStartWaitingTime(),c.getItemCount());

                ShopController.shopControllerInstance().updateCheckoutQueueSizeUI(checkoutLabel,customerQueue.size());
            }
            else{
                if(isUtilized) {
                    isUtilized = false;
                    lastUpdateTotalNotUtilizedTime =timeController.getRunningTimeInMiliseconds();
                }
                long tempTime = timeController.getRunningTimeInMiliseconds();
                totalNotUtilizedTime += tempTime - lastUpdateTotalNotUtilizedTime;
                lastUpdateTotalNotUtilizedTime = tempTime;
            }
            //only update statistics if checkout is open
            if(updateStatistics){
                totalOpenTime = timeController.getRunningTimeInMiliseconds() - checkoutOpenTime;
                System.out.println("totalopentime: "+ totalOpenTime + "   TotalNotUtilizedTime: " + totalNotUtilizedTime);
                simulationStatistics.addCheckoutStatistics(checkoutLabel,totalOpenTime,totalNotUtilizedTime);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
