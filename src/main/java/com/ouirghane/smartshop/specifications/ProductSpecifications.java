package com.ouirghane.smartshop.specifications;

import com.ouirghane.smartshop.dto.request.ProductFilter;
import com.ouirghane.smartshop.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public class ProductSpecifications {

    public static Specification<Product> hasName(String name){
        return ((root, query, cb) -> {
            if (name == null || name.trim().isEmpty()){
                return cb.conjunction();
            }
            return cb.like(root.get("name"), "%" +  name + "%");
        });
    }

    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice){
        return ((root, query, cb) -> {

            if (minPrice == null && maxPrice == null){
                return cb.conjunction();
            }

            if (minPrice != null && maxPrice != null){
                return cb.between(root.get("unitPrice"), minPrice, maxPrice);
            }

            if (minPrice != null){
                return cb.greaterThanOrEqualTo(root.get("unitPrice"), minPrice);
            }

            return cb.lessThanOrEqualTo(root.get("unitPrice"), maxPrice);
        });
    }

    public static Specification<Product> hasStockBetween(Integer minStock, Integer maxStock){
        return ((root, query, criteriaBuilder) -> {
            if (minStock == null && maxStock == null){
                return criteriaBuilder.conjunction();
            }
            if (minStock != null && maxStock != null){
                return criteriaBuilder.between(root.get("availableStock"), minStock, maxStock);
            }

            if (minStock != null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("availableStock"), minStock);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("availableStock"), maxStock);
        });
    }

    public static Specification<Product> isDeleted(Boolean deleted){
        return ((root, query, cb) -> {
            if (deleted == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("deleted"), deleted);
        });
    }

    public static Specification<Product> inStock(Boolean inStock){
        return ((root, query, cb) -> {
            if (inStock == null){
                return cb.conjunction();
            }
            if (inStock){
                return cb.greaterThan(root.get("availableStock"), 0);
            }else{
                return cb.equal(root.get("availableStock"), 0);
            }
        });
    }

    public static Specification<Product> buildSpecification(ProductFilter filter){
        return Specification.allOf(
                hasName(filter.getName()),
                hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()),
                hasStockBetween(filter.getMinStock(), filter.getMaxStock()),
                isDeleted(filter.getDeletedStatus()),
                inStock(filter.getInStock())
        );
    }

}
