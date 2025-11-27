package com.ouirghane.smartshop.repository;

import com.ouirghane.smartshop.entity.Order;
import com.ouirghane.smartshop.entity.OrderItem;
import com.ouirghane.smartshop.entity.Product;
import com.ouirghane.smartshop.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {

    List<OrderItem> findByOrder(Order order);
    
    List<OrderItem> findByOrderId(Long orderId);
    
    List<OrderItem> findByProduct(Product product);
    
    boolean existsByProduct(Product product);
    
    long countByProduct(Product product);
    
    List<OrderItem> findByProductAndOrder_Status(Product product, OrderStatus status);
    
    boolean existsByProductId(Long productId);
}