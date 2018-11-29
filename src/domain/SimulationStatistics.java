package domain;

import service.ShopController;

import java.sql.Time;
import java.util.HashMap;

public class  SimulationStatistics {
    //Total people that visited the shop
    private int totalCustomersInShop;
    //Total people currently roaming in shop;
    private volatile int totalCustomersCurrentlyInShop;
    //Total people that Pased the checkout
    private int customerCount;
    //Total people that left the shop because it was to busy
    private int customersLeft;

    private long customerTotalWaitingTimeInSeconds;
    private long customerAvgWaitingTimeInSeconds;

    //Total item processed
    private int totalProducts;
    //Total items picked up by customers
    private int totalProductsPickedUp;
    private int avgProductsPerTroley;

    private ShopController shopController;
    private boolean isClosing = false;
    private HashMap<String,CheckoutHelper> checkoutStatistics;
    private CustomerHelperService customerHelperService;
    private static SimulationStatistics instance;

    public static synchronized SimulationStatistics getInstance(TimeController timeController){
        if(instance==null){
            instance = new SimulationStatistics(timeController);
        }
        return  instance;
    }
    private SimulationStatistics(TimeController timeController){
        checkoutStatistics = new HashMap<>();customerHelperService = new CustomerHelperService(timeController);
    }
    public void setShopController(ShopController shopController){
        this.shopController = shopController;
    }


    public synchronized void customerPicksUpItem(int i){
        totalProductsPickedUp += i;
        updateGeneralStats();
    }
    public synchronized void customerEntersShop(String name){
        customerHelperService.addCustomer(name);
        totalCustomersInShop++;
        totalCustomersCurrentlyInShop++;
        updateGeneralStats();
    }
    public synchronized void customerToCheckout(String name){
        customerHelperService.addCustomerToCheckout(name);
        totalCustomersCurrentlyInShop--;
        updateGeneralStats();
    }

    public synchronized void addCheckoutStatistics(String checkoutLabel, long totalOpenTime, long totalNotUtilizedTime) {
            if (checkoutStatistics.containsKey(checkoutLabel)) {
                CheckoutHelper checkoutHelperTemp = checkoutStatistics.get(checkoutLabel);
                checkoutHelperTemp.setTotalOpenTime(totalOpenTime);
                checkoutHelperTemp.setTotalNotUtilizedTime(totalNotUtilizedTime);
            } else {
                checkoutStatistics.put(checkoutLabel, new CheckoutHelper(totalOpenTime, totalNotUtilizedTime));
            }
            updateCheckoutStats();

    }
    public synchronized void addCustomerStatistics(long customerWaitingTime, int itemCount) {
        ++customerCount;
        addCustomerWaitingTime(customerWaitingTime);
        addItemCount(itemCount);
        updateCustomerStats();
        updateProductsStats();
        updateGeneralStats();
    }

    private synchronized void addCustomerWaitingTime(long customerWaitingTime){
        customerTotalWaitingTimeInSeconds += customerWaitingTime;
        customerAvgWaitingTimeInSeconds = customerTotalWaitingTimeInSeconds/customerCount;
    }
    private synchronized void addItemCount(int itemCount) {
        totalProducts += itemCount;
        avgProductsPerTroley = totalProducts /customerCount;

    }

    public double avgCheckoutUtilization(){
        double result = 0;
        for (CheckoutHelper checkoutHelper: checkoutStatistics.values()
        ) {
            result += checkoutHelper.avgUtilizationTime();
        }

        return result/checkoutStatistics.size();
    }


    // CAllBACKS TO UI
    private synchronized void updateCustomerStats(){
        shopController.updateCustomerSimulationStats(customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds);
    }
    private synchronized void updateProductsStats(){
        shopController.updateProductSimulationStats(totalProducts,avgProductsPerTroley);
    }
    private synchronized void updateCheckoutStats(){
        shopController.updateCheckoutSimulationStats(checkoutStatistics);
    }
    private synchronized void updateGeneralStats(){
        shopController.updateGeneralStats(totalCustomersCurrentlyInShop,totalCustomersInShop,customerCount,customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds,totalProducts,totalProductsPickedUp,avgProductsPerTroley,customersLeft);


    }

    @Override
    public String toString() {
        return "SimulationStatistics{" +
                "customerCount=" + customerCount +
                ", customerTotalWaitingTimeInSeconds=" + customerTotalWaitingTimeInSeconds +
                ", customerAvgWaitingTimeInSeconds=" + customerAvgWaitingTimeInSeconds +
                ", totalProducts=" + totalProducts +
                ", avgProductsPerTroley=" + avgProductsPerTroley +
                ", avgCheckoutUtilization=" + avgCheckoutUtilization() +
                '}';
    }

    public synchronized void customerLeavesStore(String name) {
       totalCustomersCurrentlyInShop--;
       customersLeft++;
       customerHelperService.customerLeavesBecauseToBusy(name);


    }

    public synchronized void customerTotalItems(String name, int itemCount) {
        customerHelperService.addCustomerTotalItems(name,itemCount);

    }

    public synchronized void customerIsProcessed(String name, String checkoutName) {
        customerHelperService.customerIsFinished(name,checkoutName);
    }

    public synchronized void customerStartScanning(String name) {
        customerHelperService.customerStartScanning(name);
    }

    public synchronized void startShopClosing() {
     isClosing = true;
    }

    public  synchronized void customerChangedCheckout(String name) {
        customerHelperService.customerChangedCheckout(name);
    }

    public synchronized CustomerHelperService getCustomerHelperService(){
        return customerHelperService;
    }
}
class  CustomerHelper{
    //Customer name
    private String name;
    // Time Customer Arrived in Store
    private long entryTime;
    //Customer Item Count
    private int itemCount;
    //If customer left early because it was to busy
    private boolean leftEarly;
    //Customer arrived at counter
    private long customerArrivedAddCounter;
    //Customer left shop
    private long customerLeftTime;
    //name of checkout that handled customer
    private String checkoutName;
    //Customer items is going to be scanned by customer
    private long startScanningTime;
    //how many times changed checkout
    private int changedCheckout;

    public CustomerHelper(String name){
    this.name = name;
    leftEarly = false;
    }
    public void setEntryTime(long entryTime){
        this.entryTime =entryTime;
    }
    public void customerLeft(){
        this.leftEarly = true;
    };

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void addCustomerToCheckoutTime(long runningTimeInSeconds) {
        this.customerArrivedAddCounter = runningTimeInSeconds;
    }

    public void addCustomerLeftShopTime(long leftTime, String checkoutName) {
        this.customerLeftTime = leftTime;
        this.checkoutName = checkoutName;
    }

    public void addCustomerStartScanningTime(long runningTimeInSeconds) {
        this.startScanningTime = runningTimeInSeconds;
    }

    public void changedCheckout() {
        changedCheckout++;
    }

    public String getName() {
        return name;
    }
    public long getEntryTime() {
        return entryTime;
    }
    public int getItemCount() {
        return itemCount;
    }
    public boolean isLeftEarly() {
        return leftEarly;
    }
    public long getCustomerArrivedAddCounter() {
        return customerArrivedAddCounter;
    }
    public long getCustomerLeftTime() {
        return customerLeftTime;
    }
    public String getCheckoutName() {
        return checkoutName;
    }
    public long getStartScanningTime() {
        return startScanningTime;
    }
    public int getChangedCheckout() {
        return changedCheckout;
    }
}
class  CustomerHelperService{
    private HashMap<String,CustomerHelper> customers;

    private TimeController timeController;
    CustomerHelperService(TimeController timeController){
        customers = new HashMap<>();
        this.timeController = timeController;
    }

    public void addCustomer(String name){
        CustomerHelper helper = new CustomerHelper(name);
        helper.setEntryTime(timeController.getRunningTimeInSeconds());
        customers.put(name,helper);
    }
    public void customerLeavesBecauseToBusy(String name){
        customers.get(name).customerLeft();
    }

    public void addCustomerTotalItems(String name, int itemCount) {
        customers.get(name).setItemCount(itemCount);
    }

    public void addCustomerToCheckout(String name) {
        customers.get(name).addCustomerToCheckoutTime(timeController.getRunningTimeInSeconds());
    }

    public void customerIsFinished(String name, String checkoutName) {
        customers.get(name).addCustomerLeftShopTime(timeController.getRunningTimeInSeconds(),checkoutName);
    }

    public void customerStartScanning(String name) {
        customers.get(name).addCustomerStartScanningTime(timeController.getRunningTimeInSeconds());
    }

    public void customerChangedCheckout(String name) {
        customers.get(name).changedCheckout();
    }

    public HashMap<String,CustomerHelper> getCustomers(){
        return  customers;
    }
}