package com.log430.brockerx.repository;

import com.log430.brockerx.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
                SELECT o
                FROM Order o
                WHERE o.symbol = :symbol
                  AND o.side = 'SELL'                 
                  AND o.status IN ('ACK','WORKING','PARTIALLY_FILLED')
                  AND o.price <= :price          
                ORDER BY o.price DESC, o.createdAt ASC
            """) List<Order> findBuyMatchingOrders(@Param("symbol") String symbol, @Param("price") Double price);


    @Query("""
                SELECT o 
                FROM Order o 
                WHERE o.symbol = :symbol 
                  AND o.side = 'BUY'
                  AND o.status IN ('ACK','WORKING','PARTIALLY_FILLED') 
                  AND o.price >= :price 
                ORDER BY o.price ASC, o.createdAt ASC
            """) List<Order> findSellMatchingOrders(@Param("symbol") String symbol, @Param("price") Double price);


    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}

