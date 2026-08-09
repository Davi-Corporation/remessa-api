package com.api.remessa.persistence;

import com.api.remessa.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("""
    SELECT COALESCE(SUM(t.amountBrl), 0)
    FROM Transfer t
    WHERE t.sender.id = :senderId
      AND t.createdAt >= :startOfDay
      AND t.createdAt < :endOfDay""")
    BigDecimal sumAmountBrlBySenderAndDate(Long senderId,LocalDateTime startOfDay,LocalDateTime endOfDay
    );
}
