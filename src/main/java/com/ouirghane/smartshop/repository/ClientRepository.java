package com.ouirghane.smartshop.repository;

import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Optional<Client> findByUser(User user);
    
    Optional<Client> findByUserId(Long userId);
    
    Optional<Client> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Page<Client> findByClientLoyaltyLevel(ClientLoyaltyLevel level, Pageable pageable);
    
    List<Client> findTop10ByOrderByTotalSpentDesc();
    
    long countByClientLoyaltyLevel(ClientLoyaltyLevel level);
    
    @Query("SELECT c FROM Client c WHERE c.totalOrders >= :orders OR c.totalSpent >= :amount")
    List<Client> findEligibleForLoyaltyUpgrade(@Param("orders") Integer orders, @Param("amount") BigDecimal amount);
}