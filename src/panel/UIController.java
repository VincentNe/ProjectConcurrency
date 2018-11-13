package panel;

import domain.CheckoutHelper;
import service.ShopController;

import java.util.HashMap;

public class UIController  {
    private static UIController instance;
    private CheckoutSection checkoutSection;
    private TimeBar timeBar;
    private ShopController shopController;
    private SimulationSettings simulationSettings;
    private GeneralStats generalStats;

    private UIController(){
        shopController = ShopController.shopControllerInstance();
    }

    public static synchronized UIController UIControllerinstance(){
        if(instance==null) instance = new UIController();
        return  instance;
    }
    public void addCheckoutSection(CheckoutSection checkoutSection){
        this.checkoutSection = checkoutSection;
    }

    public void addCheckout(String UID, int scannerDelay){
        shopController.addCheckout(UID,scannerDelay);
        checkoutSection.addCheckout(UID);
    }

    public void addTimerBar(TimeBar timeBar) {
        this.timeBar = timeBar;
    }

    public void addGeneralStats(GeneralStats generalStats){this.generalStats = generalStats;}

    public void startTimer() {
        simulationSettings.setSettings();
        timeBar.startTimer();
        shopController.startTime();
    }

    public void setSimulationSettings(int minCustomerLoad, int maxCustomerLoad, double customerSpawnMean, double customerSpawnVariance ){
        shopController.setSettings(minCustomerLoad,maxCustomerLoad, customerSpawnMean,customerSpawnVariance);
    }
    public void stopTimer(){
        timeBar.stopTimer();
    }
    public long getTime(){
        return shopController.getTime();
    }
    public void updateCheckoutQueueSize(String UID,int size ){
        checkoutSection.setCheckoutQueueSize(UID,size);
    }

    public void addSimulationSettings(SimulationSettings simulationSettings) {
        this.simulationSettings = simulationSettings;
    }

    public void updateProductSimulationStats(int totalProducts, int avgProductsPerTroley) {
        //TODO forward product stats to UI
    }

    public void updateCheckoutSimulationStats(HashMap<String, CheckoutHelper> checkoutStatistics) {
        checkoutSection.addCheckoutStatistics(checkoutStatistics);
    }

    public void updateCustomerSimulationStats(long customerTotalWaitingTimeInSeconds, long customerAvgWaitingTimeInSeconds) {
        //TODO forward customer stats to UI
    }

    public void updateGeneralStats(int totalCustomersCurrentlyInShop, int totalCustomersInShop, int customerCount, long customerTotalWaitingTimeInSeconds, long customerAvgWaitingTimeInSeconds, int totalProducts, int totalProductsPickedUp, int avgProductsPerTroley) {
        generalStats.updateGeneralStats(totalCustomersCurrentlyInShop,totalCustomersInShop,customerCount,customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds,totalProducts,totalProductsPickedUp,avgProductsPerTroley);
    }

    public void updateCheckoutScanningProcess(String checkoutLabel, int currentItem, int totalCustomerItems) {
        checkoutSection.updateCheckoutScanningProcess(checkoutLabel,currentItem,totalCustomerItems);
    }

    public void closeCheckout(String checkoutName) {
        shopController.closeCheckout(checkoutName);
    }

    public void openCheckout(String checkoutName) {
        shopController.openCheckout(checkoutName);
    }
}
