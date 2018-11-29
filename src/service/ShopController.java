package service;

import domain.*;
import org.omg.CORBA.WrongTransaction;
import panel.UIController;

import java.util.HashMap;

public class ShopController {
    private CheckoutController checkoutController;
    private CustomerPoolController customers;
    private TimeController timeController;
    private SimulationSettings settings;
    private SimulationStatistics simulationStatistics;
    private Spawner spawner;


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
        simulationStatistics = SimulationStatistics.getInstance(timeController);
        simulationStatistics.setShopController(this);

    }
    public void addUIController(UIController UIController){
        this.uiController = UIController;
    }
    public void addCustomer(Customer customer){
        customers.addCustomer(customer);
    }
    public void setSettings(int minCustomerLoad, int maxCustomerLoad,double customerSpawnMean, double customerSpawnVariance ){
       if(settings ==null) settings = new SimulationSettings(minCustomerLoad,maxCustomerLoad,customerSpawnMean,customerSpawnVariance);
        else{
            settings.setMinCustomerLoad(minCustomerLoad);
            settings.setMaxCustomerSettings(maxCustomerLoad);
            settings.setCustomerSpawnMean(customerSpawnMean);
            settings.setCustomerSpawnVariance(customerSpawnVariance);
        }
    }
    /**
     * Start the Simulation
     */
    public void startTime(){

        timeController.startTimer();
        startSimulation();
        new WriteFile();

    }

    private void startSimulation() {
        checkoutController.startCheckouts();
        this.spawner = new Spawner(timeController,settings,checkoutController,simulationStatistics,this);
        Thread threadSpawner = new Thread(spawner);
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

    public synchronized void addCheckoutStatistics(String checkoutLabel, long totalUpTime, long totalDownTime) {
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

    public void updateGeneralStats(int totalCustomersCurrentlyInShop, int totalCustomersInShop, int customerCount, long customerTotalWaitingTimeInSeconds, long customerAvgWaitingTimeInSeconds, int totalProducts, int totalProductsPickedUp, int avgProductsPerTroley,int customersLeftShop) {
        uiController.updateGeneralStats(totalCustomersCurrentlyInShop,totalCustomersInShop,customerCount,customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds,totalProducts,totalProductsPickedUp,avgProductsPerTroley,customersLeftShop);
    }

    public synchronized void updateCheckoutScanningProcess(String checkoutLabel, int currentItem, int totalCustomerItems) {
        uiController.updateCheckoutScanningProcess(checkoutLabel,currentItem,totalCustomerItems);
    }

    public void closeCheckout(String checkoutName) {
        checkoutController.closeCheckout(checkoutName);
    }

    public void openCheckout(String checkoutName) {
        checkoutController.openCheckout(checkoutName);

    }

    public boolean IsCheckoutNameAvailable(String uid) {
        return checkoutController.IsCheckoutNameAvailable(uid);
    }

    public void addBulk(int valueOf) {
        spawner.spawnBulk(valueOf);
    }

    public void closeShop() {
        spawner.stopSpawning();
    }

    public void shopIsFinished() {
        checkoutController.stopCheckouts();
        timeController.stopTimer();
        uiController.shopIsFinished();
    }

    public void saveSimulation() {
        WriteFile file = new WriteFile();
        file.saveSimulation(simulationStatistics);
    }
}

