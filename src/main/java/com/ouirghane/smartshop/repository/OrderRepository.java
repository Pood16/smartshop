package com.ouirghane.smartshop.repository;

import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.Order;
import com.ouirghane.smartshop.entity.PromoCode;
import com.ouirghane.smartshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

//    Page<Order> findByClient(Client client, Pageable pageable);
//
//    Page<Order> findByClientId(Long clientId, Pageable pageable);
//
//    List<Order> findByClientOrderByOrderDateDesc(Client client);
//
//    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
//
//    Page<Order> findByStatusIn(List<OrderStatus> statuses, Pageable pageable);
//
//    long countByStatus(OrderStatus status);
//
//    List<Order> findByRemainingAmountGreaterThan(BigDecimal amount);
//
//    List<Order> findByRemainingAmountEquals(BigDecimal zero);
//
//    List<Order> findByStatusAndRemainingAmountEquals(OrderStatus status, BigDecimal zero);
//
//    Page<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
//
//    List<Order> findByClientAndOrderDateBetween(Client client, LocalDateTime start, LocalDateTime end);
//
//    List<Order> findByClientAndStatusOrderByOrderDateDesc(Client client, OrderStatus status);
//
//    List<Order> findByPromoCode(PromoCode promoCode);
//
//    boolean existsByPromoCodeAndStatus(PromoCode promoCode, OrderStatus status);
//
//    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.client = :client AND o.status = :status")
//    BigDecimal getTotalSpentByClientAndStatus(@Param("client") Client client, @Param("status") OrderStatus status);
//
//    @Query("SELECT COUNT(o) FROM Order o WHERE o.client = :client AND o.status = :status")
//    Long getOrderCountByClientAndStatus(@Param("client") Client client, @Param("status") OrderStatus status);
//
//    Page<Order> findByClientIdOrderByCreatedAtDesc(Long clientId, Pageable pageable);
}