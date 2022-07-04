package com.victor.orderservice.service;

import com.victor.orderservice.dto.InventoryResponse;
import com.victor.orderservice.dto.OrderLineItemsDto;
import com.victor.orderservice.dto.OrderRequest;
import com.victor.orderservice.model.Order;
import com.victor.orderservice.model.OrderLineItems;
import com.victor.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsList().stream()
                .map(this::mapToDto).toList();
        order.setOrderLineItems(orderLineItems);
        // call inventory service and place order if product is in stock

        List<String> skuCodes = order.getOrderLineItems().stream().map(
                OrderLineItems::getSkuCode
        ).toList();

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                //.uri("http://localhost:8082/api/inventory",
                .uri("http://inventory-service/api/inventory/",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock) {
        orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Product is not in stock, Please try again");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}