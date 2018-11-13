package domain;

import service.ShopController;

import java.util.HashMap;

public class  SimulationStatistics {
    //Total people that visited the shop
    private int totalCustomersInShop;
    //Total people currently roaming in shop;
    private volatile int totalCustomersCurrentlyInShop;
    //Total people that Pased the checkout
    private int customerCount;

    private long customerTotalWaitingTimeInSeconds;
    private long customerAvgWaitingTimeInSeconds;

    //Total item processed
    private int totalProducts;
    //Total items picked up by customers
    private int totalProductsPickedUp;
    private int avgProductsPerTroley;

    private TimeController timeController;
    private ShopController shopController;

    private HashMap<String,CheckoutHelper> checkoutStatistics;

    private static SimulationStatistics instance;
    public static synchronized SimulationStatistics getInstance(){
        if(instance==null){
            instance = new SimulationStatistics();
        }
        return  instance;
    }
    private SimulationStatistics(){
        checkoutStatistics = new HashMap<>();
    }
    public void setShopController(ShopController shopController){
        this.shopController = shopController;
    }

    public void customerPicksUpItem(int i){
        totalProductsPickedUp += i;
        updateGeneralStats();
    }
    public void customerEntersShop(){
        totalCustomersInShop++;
        totalCustomersCurrentlyInShop++;
        updateGeneralStats();
    }
    public void customerToCheckout(){
        totalCustomersCurrentlyInShop--;
        updateGeneralStats();
    }

    public synchronized void addCheckoutStatistics(String checkoutLabel, long totalOpenTime, long totalNotUtilizedTime) {
        synchronized (checkoutStatistics) {
            if (checkoutStatistics.containsKey(checkoutLabel)) {
                CheckoutHelper checkoutHelperTemp = checkoutStatistics.get(checkoutLabel);
                checkoutHelperTemp.setTotalOpenTime(totalOpenTime);
                checkoutHelperTemp.setTotalNotUtilizedTime(totalNotUtilizedTime);
            } else {
                checkoutStatistics.put(checkoutLabel, new CheckoutHelper(totalOpenTime, totalNotUtilizedTime));
            }
            updateCheckoutStats();
        }
    }
    public synchronized void addCustomerStatistics(long customerWaitingTime, int itemCount) {
        ++customerCount;
        addCustomerWaitingTime(customerWaitingTime);
        addItemCount(itemCount);
        updateCustomerStats();
        updateProductsStats();
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
        shopController.updateGeneralStats(totalCustomersCurrentlyInShop,totalCustomersInShop,customerCount,customerTotalWaitingTimeInSeconds,customerAvgWaitingTimeInSeconds,totalProducts,totalProductsPickedUp,avgProductsPerTroley);


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
}
