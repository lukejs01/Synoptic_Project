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
    @Query(value = " update Card c set c.live_Status = :status where c.card_Id = :cardId", nativeQuery = true)
    void changeLive(@Param("status") boolean status, @Param("cardId") String cardId);

    @Modifying
    @Query(value = " update Card c set c.last_Login = :lastLogin where c.card_Id = :cardId", nativeQuery = true)
    void updateLastLogin(@Param("lastLogin") Instant lastLogin, @Param("cardId") String cardId);

    @Modifying
    @Query(value = " update Card c set c.balance = :newBalance where c.card_id = :cardId", nativeQuery = true)
    void updateBalance(@Param("newBalance") Double newBalance, @Param("cardId") String cardId);


    // Only used for testing - currently not being used
    @Modifying
    @Query(value = " update Card c set c.last_Login = :newInstant where c.card_id = :cardId", nativeQuery = true)
    void updateLastLoginForTest(@Param("newInstant") Instant lastLogin, @Param("cardId") String cardId);
}
