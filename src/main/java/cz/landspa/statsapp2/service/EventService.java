package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.event.*;
import cz.landspa.statsapp2.model.entity.event.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EventService {

    List<Event> getGameEvents(Long gameId);

    Event addGoal(Long gameId, GoalScoredDTO dto);
    Event addConcededGoal(Long gameId, GoalConcededDTO dto);
    Event addPenalty(Long gameId, PenaltyDTO dto);
    Event addOpponentPenalty(Long gameId, OpponentPenaltyDTO dto);

    Event updateEvent(Long id, Object dto);


    Optional<Event> getById(Long eventId);

    void deleteEvent(Long eventId);




}
