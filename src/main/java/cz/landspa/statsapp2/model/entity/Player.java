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
    Long id;

    @NotBlank(message = "Jméno musí být vyplněno")
    String name;

    @NotBlank(message = "Příjmení musí být vyplněno")
    String surname;


    @Min(value = 1, message = "Číslo musí být větší než 0")
    @Max(value = 99, message = "Číslo musí být menší než 100")
    Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    Team team;

    @ManyToOne
    Position position;

}
