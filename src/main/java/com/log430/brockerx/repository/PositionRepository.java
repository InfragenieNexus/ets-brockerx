package com.log430.brockerx.repository;

import com.log430.brockerx.entity.Position;
import com.log430.brockerx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByUserIdAndSymbol(Long userId, String symbol);
}
