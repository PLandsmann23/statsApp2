package cz.landspa.statsapp2.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(name = "Roster", columnNames = {"game_id", "player_id"})})
public class Roster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Game game;

    @ManyToOne
    private Player player;

    private Integer gameNumber;

    @Min(value = 0)
    @Max(value = 5)
    private Integer line = 0;

    private boolean activeGk;
}
