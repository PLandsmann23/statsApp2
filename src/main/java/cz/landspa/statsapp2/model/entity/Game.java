package cz.landspa.statsapp2.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team team;

    @NotBlank(message = "Soupeř musí být vyplněn")
    private String opponent;

    private LocalDate date;

    private LocalTime time;

    private String venue;

    @Min(value = 1, message = "Počet třetin musí být alespoň 1")
    private Integer periods = 3;

    @Min(value = 1, message = "Délka třetiny musí být alespoň 1 minuta")
    private Integer periodLength = 20;

    @Min(value = 1)
    private Integer currentPeriod = 1;

}
