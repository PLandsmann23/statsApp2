package cz.landspa.statsapp2.model.entity.stats;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import cz.landspa.statsapp2.model.entity.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(name = "ShotsRecord", columnNames = {"game_id", "period"})})
public class ShotsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Game game;

    private Integer period;

    private Long shots =0L;
}
