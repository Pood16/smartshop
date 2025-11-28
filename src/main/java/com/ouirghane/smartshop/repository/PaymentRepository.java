package com.ouirghane.smartshop.repository;


import com.ouirghane.smartshop.entity.Order;
import com.ouirghane.smartshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {


    List<Payment> getByOrderId(Long orderId);

    Long countByOrderId(Long orderId);

}