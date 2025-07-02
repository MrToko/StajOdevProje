package com.example.customerapi.service;

import com.example.customerapi.dto.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    // Bellekte müşteri verilerini tutacağız
    private final Map<Long, CustomerDTO> customerMap = new HashMap<>();
    private long idCounter = 1;

    // Yeni müşteri oluştur
    public CustomerDTO createCustomer(CustomerDTO customer) {
        customer.setId(idCounter++);
        customerMap.put(customer.getId(), customer);
        return customer;
    }

    // ID’ye göre müşteri getir
    public Optional<CustomerDTO> getCustomerById(Long id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    // Tüm müşterileri getir
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    // Müşteri güncelle
    public Optional<CustomerDTO> updateCustomer(Long id, CustomerDTO updatedCustomer) {
        if (customerMap.containsKey(id)) {
            updatedCustomer.setId(id);
            customerMap.put(id, updatedCustomer);
            return Optional.of(updatedCustomer);
        }
        return Optional.empty();
    }

    // Müşteri sil
    public boolean deleteCustomer(Long id) {
        return customerMap.remove(id) != null;
    }
}
