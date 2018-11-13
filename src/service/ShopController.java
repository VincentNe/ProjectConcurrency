package service;

import domain.*;
import panel.UIController;

import java.util.HashMap;

public class ShopController {
    private CheckoutController checkoutController;
    private CustomerPoolController customers;
    private TimeController timeController;
    private SimulationSettings settings;
    private SimulationStatistics simulationStatistics;



    private UIController uiController;


    private  static ShopController shopController;
    public static  ShopController shopControllerInstance(){
        if(shopController==null)shopController = new ShopController();
        return  shopController;

    }

    private ShopController(){
        checkoutController= new CheckoutController();
        customers = new CustomerPoolController();
        timeController = new TimeController();
        simulationStatistics = SimulationStatistics.getInstance();
        simulationStatistics.setShopController(this);

    }
    public void addUIController(UIController UIController){
        this.uiController = UIController;
    }
    public void addCustomer(Customer customer){
        customers.addCustomer(customer);
    }
    public void setSettings(int minCustomerLoad, int maxCustomerLoad,double customerSpawnMean, double customerSpawnVariance ){
        settings = new SimulationSettings(minCustomerLoad,maxCustomerLoad,customerSpawnMean,customerSpawnVariance);
    }
    /**
     * Start the Simulation
     */
    public void startTime(){

        timeController.startTimer();
        startSimulation();


    }

    private void startSimulation() {
        checkoutController.startCheckouts();
        Thread threadSpawner = new Thread(new Spawner(timeController,settings,checkoutController,simulationStatistics));
        threadSpawner.start();
    }

    public void stopTime(){
        timeController.stopTimer();
    }
    public long getTime(){
        return timeController.getRunningTimeInSeconds();
    }


    public void addCheckout(String UID, int scannerDelay) {

        checkoutController.addCheckout(UID,new CheckoutImpl(10,scannerDelay,10,10,UID,timeController,simulationStatistics));

    }

    public void updateCheckoutQueueSizeUI(String checkoutLabel, int size) {
        uiController.updateCheckoutQueueSize(checkoutLabel,size);
    }




    public void addCustomerStatistics(long l, int itemCount) {
        simulationStatistics.addCustomerStatistics(l,itemCount);
    }

    public void addCheckoutStatistics(String checkoutLabel, long totalUpTime, long totalDownTime) {
        simulationStatistics.addCheckoutStatistics( checkoutLabel,totalUpTime,totalDownTime);
    }
    public void updateCustomerSimulationStats(long customerTotalWaitingTimeInSeconds, long customerAvgWaitingTimeInSeconds){
        uiController.updateCustomerSimulationStats(customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds);
    }
    public void updateCheckoutSimulationStats(HashMap<String, CheckoutHelper> checkoutStatistics){
        uiController.updateCheckoutSimulationStats(checkoutStatistics);
    }
    public void updateProductSimulationStats(int totalProducts, int avgProductsPerTroley) {
        uiController.updateProductSimulationStats(totalProducts,avgProductsPerTroley);
    }

    public void updateGeneralStats(int totalCustomersCurrentlyInShop, int totalCustomersInShop, int customerCount, long customerTotalWaitingTimeInSeconds, long customerAvgWaitingTimeInSeconds, int totalProducts, int totalProductsPickedUp, int avgProductsPerTroley) {
        uiController.updateGeneralStats(totalCustomersCurrentlyInShop,totalCustomersInShop,customerCount,customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds,totalProducts,totalProductsPickedUp,avgProductsPerTroley);
    }

    public void updateCheckoutScanningProcess(String checkoutLabel, int currentItem, int totalCustomerItems) {
        uiController.updateCheckoutScanningProcess(checkoutLabel,currentItem,totalCustomerItems);
    }

    public void closeCheckout(String checkoutName) {
        checkoutController.closeCheckout(checkoutName);
    }

    public void openCheckout(String checkoutName) {
        checkoutController.openCheckout(checkoutName);

    }
}

