package com.synopticproject.synopticproject.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    @Modifying
    @Query(value = " update Card c set c.liveStatus = :status where c.cardId = :cardId", nativeQuery = true)
    void changeLive(@Param("status") boolean status, @Param("cardId") String cardId);

    @Modifying
    @Query(value = " update Card c set c.lastLogin = :lastLogin where c.cardId = :cardId", nativeQuery = true)
    void updateLastLogin(@Param("lastLogin") Instant lastLogin, @Param("cardId") String cardId);

    @Query(value = " select c.lastLogin from Card c where c.cardId = :cardId")
    Instant getLastLogin(@Param("cardId") String cardId);
}
