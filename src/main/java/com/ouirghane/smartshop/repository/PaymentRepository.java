package com.ouirghane.smartshop.repository;



import com.ouirghane.smartshop.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {


    Page<Payment> findPaymentsByOrderId(Long orderId, Pageable pageable);

    Long countByOrderId(Long orderId);


}