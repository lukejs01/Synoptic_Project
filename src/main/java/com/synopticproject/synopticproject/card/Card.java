package com.synopticproject.synopticproject.card;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Random;

@Entity
@Data
@NoArgsConstructor
public class Card {

    @Id
    private String cardId;

    @GeneratedValue
    private Long employeeId;

    @Size(max = 64)
    @NotEmpty
    private String name;

    @Size(max = 128)
    @NotEmpty
    private String email;

    @NotEmpty
    private int mobileNumber;

    private int pin;


}
