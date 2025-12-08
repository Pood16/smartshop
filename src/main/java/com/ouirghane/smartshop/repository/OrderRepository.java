package com.ouirghane.smartshop.repository;


import com.ouirghane.smartshop.entity.Order;
import com.ouirghane.smartshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Page<Order> findByClientId(Long clientId, Pageable pageable);
    boolean existsByClientIdAndStatus(Long clientId, OrderStatus status);

}