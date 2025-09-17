package cz.landspa.statsapp2.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Jméno musí být vyplněno")
    private String name;

    @NotBlank(message = "Příjmení musí být vyplněno")
    private String surname;


    @Min(value = 1, message = "Číslo musí být větší než 0")
    @Max(value = 99, message = "Číslo musí být menší než 100")
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    private Team team;

    @ManyToOne
    private Position position;

}
