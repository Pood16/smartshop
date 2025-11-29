package com.ouirghane.smartshop.repository;

import com.ouirghane.smartshop.entity.PromoCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long>, JpaSpecificationExecutor<PromoCode> {

    Optional<PromoCode> findByCode(String code);
    
    boolean existsByCode(String code);
    
    Page<PromoCode> findByActiveTrue(Pageable pageable);

}