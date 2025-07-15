package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.DTO.event.*;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.event.*;
import cz.landspa.statsapp2.repository.EventRepository;
import cz.landspa.statsapp2.repository.GameRepository;
import cz.landspa.statsapp2.repository.RosterRepository;
import cz.landspa.statsapp2.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RosterRepository rosterRepository;
    private final GameRepository gameRepository;

    public EventServiceImpl(EventRepository eventRepository, RosterRepository rosterRepository, GameRepository gameRepository) {
        this.eventRepository = eventRepository;
        this.rosterRepository = rosterRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Event> getGameEvents(Long gameId) {
        return eventRepository.findAllByGameIdOrderByTimeAsc(gameId);
    }

    @Override
    public Event addGoal(Long gameId, GoalScoredDTO dto) {
        Game game = gameRepository.findById(gameId).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

        GoalScored goal = new GoalScored();
        goal.setGame(game);
        goal.setTime(dto.time());
        goal.setSituation(dto.situation());

        if (dto.scorerId() != null)
            goal.setScorer(rosterRepository.findById(dto.scorerId()).orElse(null));

        if (dto.assistIds() != null)
            goal.setAssists(dto.assistIds().stream()
                    .map(id -> rosterRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .toList());

        if (dto.onIceIds() != null)
            goal.setOnIce(dto.onIceIds().stream()
                    .map(id -> rosterRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .toList());

        return eventRepository.save(goal);
    }

    @Override
    public Event addConcededGoal(Long gameId, GoalConcededDTO dto) {
        Game game = gameRepository.findById(gameId).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

        GoalConceded goal = new GoalConceded();
        goal.setGame(game);
        goal.setTime(dto.time());
        goal.setSituation(dto.situation());

        if (dto.inGoalId() != null)
            goal.setInGoal(rosterRepository.findById(dto.inGoalId()).orElse(null));

        if (dto.onIceIds() != null)
            goal.setOnIce(dto.onIceIds().stream()
                    .map(id -> rosterRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .toList());

        return eventRepository.save(goal);
    }

    @Override
    public Event addPenalty(Long gameId, PenaltyDTO dto) {
        Game game = gameRepository.findById(gameId).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

        Penalty penalty = new Penalty();
        penalty.setGame(game);
        penalty.setTime(dto.time());
        penalty.setReason(dto.reason());
        penalty.setMinutes(dto.minutes());
        penalty.setCoincidental(dto.coincidental());

        if (dto.playerId() != null)
            penalty.setPlayer(rosterRepository.findById(dto.playerId()).orElse(null));

        return eventRepository.save(penalty);
    }

    @Override
    public Event addOpponentPenalty(Long gameId, OpponentPenaltyDTO dto) {
        Game game = gameRepository.findById(gameId).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

        OpponentPenalty penalty = new OpponentPenalty();
        penalty.setGame(game);
        penalty.setTime(dto.time());
        penalty.setMinutes(dto.minutes());
        penalty.setCoincidental(dto.coincidental());

        return eventRepository.save(penalty);
    }

    @Override
    public Event updateEvent(Long id, Object dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event nenalezen"));

        if (event instanceof GoalScored gs && dto instanceof GoalScoredDTO gsdto) {
            updateGoalScored(gs, gsdto);
        } else if (event instanceof GoalConceded gc && dto instanceof GoalConcededDTO gcdto) {
            updateGoalConceded(gc, gcdto);
        } else if (event instanceof Penalty p && dto instanceof PenaltyDTO pdto) {
            updatePenalty(p, pdto);
        } else if (event instanceof OpponentPenalty op && dto instanceof OpponentPenaltyDTO odto) {
            updateOpponentPenalty(op, odto);
        } else {
            throw new IllegalArgumentException("Nekompatibilní typ DTO");
        }

        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> getById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }


    private void updateGoalScored(GoalScored existing, GoalScoredDTO dto) {
        existing.setTime(dto.time());
        existing.setSituation(dto.situation());

        existing.setScorer(dto.scorerId() != null ? rosterRepository.findById(dto.scorerId()).orElse(null) : null);

        existing.setAssists(dto.assistIds() != null
                ? dto.assistIds().stream()
                .map(id -> rosterRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                : new ArrayList<>());

        existing.setOnIce(dto.onIceIds() != null
                ? dto.onIceIds().stream()
                .map(id -> rosterRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                : new ArrayList<>());

    }

    private void updateGoalConceded(GoalConceded existing, GoalConcededDTO dto) {
        existing.setTime(dto.time());
        existing.setSituation(dto.situation());

        existing.setInGoal(dto.inGoalId() != null ? rosterRepository.findById(dto.inGoalId()).orElse(null) : null);

        existing.setOnIce(dto.onIceIds() != null
                ? dto.onIceIds().stream().map(id -> rosterRepository.findById(id).orElse(null))
                .filter(Objects::nonNull).toList()
                : List.of());
    }

    private void updatePenalty(Penalty existing, PenaltyDTO dto) {
        existing.setTime(dto.time());
        existing.setMinutes(dto.minutes());
        existing.setReason(dto.reason());
        existing.setCoincidental(dto.coincidental());

        existing.setPlayer(dto.playerId() != null ? rosterRepository.findById(dto.playerId()).orElse(null) : null);
    }

    private void updateOpponentPenalty(OpponentPenalty existing, OpponentPenaltyDTO dto) {
        existing.setTime(dto.time());
        existing.setMinutes(dto.minutes());
        existing.setCoincidental(dto.coincidental());
    }
}