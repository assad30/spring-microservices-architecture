package com.mywork.miceroservices.order.service;

import com.mywork.miceroservices.order.client.InventoryClient;
import com.mywork.miceroservices.order.dto.OrderRequest;
import com.mywork.miceroservices.order.model.Order;
import com.mywork.miceroservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    public void placeOrder(OrderRequest orderRequest) {

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());
        if(isProductInStock){
            var order = mapToOrder(orderRequest);
            orderRepository.save(order);
        }else {
            throw new RuntimeException("Product with SkuCode "+orderRequest.skuCode() + "in not in Stock");
        }

    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }
}
