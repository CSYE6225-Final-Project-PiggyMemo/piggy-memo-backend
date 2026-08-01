package com.csye6225.piggymemo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csye6225.piggymemo.dto.DailySpending;
import com.csye6225.piggymemo.entity.Spendings;

import java.time.OffsetDateTime;
import java.util.List;

public interface SpendingsRepository extends JpaRepository<Spendings, Long> {
    Page<Spendings> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query(value = """
        SELECT CAST(s.created_at AT TIME ZONE :timeZone AS date) AS date,
               SUM(s.amount) AS amount
        FROM spendings s
        WHERE s.user_id = :userId
          AND s.amount > 0
          AND s.created_at >= :monthStart
          AND s.created_at < :nextMonthStart
        GROUP BY 1
        ORDER BY 1
        """, nativeQuery = true)
    List<DailySpending> findDailySpendingByMonth(@Param("userId") Long userId, @Param("monthStart") OffsetDateTime monthStart, @Param("nextMonthStart") OffsetDateTime nextMonthStart, @Param("timeZone") String timeZone);
}
