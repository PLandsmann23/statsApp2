package cz.landspa.statsapp2.model.entity.stats;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Roster;
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
@Table(uniqueConstraints = {@UniqueConstraint(name = "SavesRecord", columnNames = {"game_id", "goalkeeper_id", "period"})})
public class SavesRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    Game game;

    @ManyToOne
    Roster goalkeeper;

    Integer period;

    Long saves = 0L;
}
