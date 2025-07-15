package cz.landspa.statsapp2.model.DTO.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GoalScoredDTO.class, name = "GoalScored"),
        @JsonSubTypes.Type(value = GoalConcededDTO.class, name = "GoalConceded"),
        @JsonSubTypes.Type(value = PenaltyDTO.class, name = "Penalty"),
        @JsonSubTypes.Type(value = OpponentPenaltyDTO.class, name = "OpponentPenalty")
})
public interface EventDTO {
}


