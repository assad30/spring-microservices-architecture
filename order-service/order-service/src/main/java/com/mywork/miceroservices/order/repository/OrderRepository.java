package com.mywork.miceroservices.order.repository;

import com.mywork.miceroservices.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
