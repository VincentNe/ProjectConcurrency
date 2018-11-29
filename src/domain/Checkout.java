package domain;

public interface Checkout extends Runnable{

    void scanItem();
    String getName();
    int getQueueSize();
    void addCustomer(Customer customer);
    void addCustomerChangedCheckout(Customer customer);
    boolean isClosed();
    void closeCheckout();
    void openCheckout();

    int[] getCustomerPosition(String name);

    boolean removeCustomer(String name);

    void stopRunning();
}
