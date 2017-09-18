package org.restbucks.tdd.domain.ordering;

import org.restbucks.tdd.domain.ordering.Order.Identity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    public Order findOne(Identity identity) {
        return null;
    }


    public List<Order> findByStatus(String status) {
        return null;
    }
}
