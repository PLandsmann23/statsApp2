package cz.landspa.statsapp2.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class UserSetting {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    @Min(value = 1, message = "Počet herních částí musí být alespoň 1")
    private Integer defaultPeriods = 3;

    @Min(value = 1, message = "Délka herní části musí být alespoň 1 minuta")
    private Integer defaultPeriodLength = 20;
}

