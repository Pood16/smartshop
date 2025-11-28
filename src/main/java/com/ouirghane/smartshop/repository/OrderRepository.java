package com.ouirghane.smartshop.repository;

import com.ouirghane.smartshop.dto.response.OrderResponseDto;
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

    Page<Order> findByClientId(Long clientId, Pageable pageable);

}