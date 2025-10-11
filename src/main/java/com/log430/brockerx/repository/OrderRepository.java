package com.log430.brockerx.repository;

import com.log430.brockerx.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdempotencyKey(String idempotencyKey);

    @Query("""
                SELECT o FROM Order o
                WHERE o.symbol = :symbol
                  AND o.side = :oppositeSide
                  AND o.status IN ('ACK','WORKING','PARTIALLY_FILLED')
                  AND (
                      (:side = 'BUY' AND o.price <= :price)
                      OR
                      (:side = 'SELL' AND o.price >= :price)
                  )
                ORDER BY o.price ASC, o.createdAt ASC
            """) List<Order> findMatchingOrders(@Param("symbol") String symbol,
                                                @Param("oppositeSide") String oppositeSide,
                                                @Param("price") Double price, @Param("side") String side);
}

