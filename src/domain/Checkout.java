package domain;

public interface Checkout extends Runnable{

    void scanItem();
    String getName();
    int getQueueSize();
    void addCustomer(Customer customer);
    boolean isClosed();
    void closeCheckout();
    void openCheckout();

}
