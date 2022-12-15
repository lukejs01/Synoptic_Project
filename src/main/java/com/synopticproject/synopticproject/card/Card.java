package com.synopticproject.synopticproject.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


@Entity
@Data
@NoArgsConstructor
public class Card {

    @Id
    private String cardId;

    @NotNull
    private Long employeeId;

    @Size(max = 64)
    @NotEmpty
    private String name;

    @Size(max = 128)
    @NotEmpty
    private String email;

    @NotEmpty
    private String mobileNumber;

    @NotNull
    private int pin;

    @Column
    private Instant lastLogin = Instant.now();

    @Column
    private boolean liveStatus = false;

    @Column
    private Double balance = 0.0;

}
