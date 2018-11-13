package service;

import domain.Customer;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class CustomerPoolController {
    private ArrayDeque<Customer> customers;

    public CustomerPoolController(){
        customers = new ArrayDeque<>();
    }

    public  void addCustomer(Customer customer){
        customers.add(customer);
    }

    public Customer getCustomer(){
        return customers.remove();
    }
    public boolean customersInShop(){System.out.println("Size shop: " + customers.size());return !customers.isEmpty();
    }



}
