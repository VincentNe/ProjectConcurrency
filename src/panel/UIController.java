package panel;

import domain.CheckoutHelper;
import service.ShopController;

import java.util.HashMap;

public class UIController  {
    private static UIController instance;
    private CheckoutSection checkoutSection;
    private TimeBar timeBar;
    private LeftPanel leftPanel;
    private ShopController shopController;
    private SimulationSettings simulationSettings;
    private GeneralStats generalStats;
    private boolean settingsSet = false;

    public void saveSimulation() {
        if(state ==  SimulationStates.finished){
            shopController.saveSimulation();
        }
    }

    public void addLeftPanel(LeftPanel leftPanel) {
        this.leftPanel = leftPanel;
    }

    private enum SimulationStates {not_running,running,stopped,finished}
    private SimulationStates state;

    private UIController(){
        shopController = ShopController.shopControllerInstance();
        state = SimulationStates.not_running;
    }

    public static synchronized UIController UIControllerinstance(){
        if(instance==null) instance = new UIController();
        return  instance;
    }

    public void addBulk(int valueOf) {
        if(state == SimulationStates.running) {
            shopController.addBulk(valueOf);
        }
    }

    public void addCheckoutSection(CheckoutSection checkoutSection){
        this.checkoutSection = checkoutSection;
    }

    public boolean addCheckout(String UID, int scannerDelay){
        System.out.println(shopController.IsCheckoutNameAvailable(UID));
        if(shopController.IsCheckoutNameAvailable(UID)) {
            shopController.addCheckout(UID, scannerDelay);
            checkoutSection.addCheckout(UID);
            return true;
        }
        return false;
    }

    public void addTimerBar(TimeBar timeBar) {
        this.timeBar = timeBar;
    }

    public void addGeneralStats(GeneralStats generalStats){this.generalStats = generalStats;}

    public void startTimer() {
        if(settingsSet) {
            leftPanel.simulationStarted();
            state = SimulationStates.running;
            timeBar.startTimer();
            shopController.startTime();
        }
    }

    public void setSimulationSettings(int minCustomerLoad, int maxCustomerLoad, double customerSpawnMean, double customerSpawnVariance ){
        settingsSet = true;
        shopController.setSettings(minCustomerLoad,maxCustomerLoad, customerSpawnMean,customerSpawnVariance);
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

    public void updateGeneralStats(int totalCustomersCurrentlyInShop, int totalCustomersInShop, int customerCount, long customerTotalWaitingTimeInSeconds, long customerAvgWaitingTimeInSeconds, int totalProducts, int totalProductsPickedUp, int avgProductsPerTroley,int customersLeftShop) {
        generalStats.updateGeneralStats(totalCustomersCurrentlyInShop,totalCustomersInShop,customerCount,customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds,totalProducts,totalProductsPickedUp,avgProductsPerTroley,customersLeftShop);
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

    public void closeShop() {
        shopController.closeShop();
        state = SimulationStates.stopped;
    }
    public void shopIsFinished() {
        state = SimulationStates.finished;
        timeBar.stopTimer();
        leftPanel.simulationIsDone();
    }
}
