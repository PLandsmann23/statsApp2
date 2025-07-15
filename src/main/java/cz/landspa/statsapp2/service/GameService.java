package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.game.GameFullInfoDTO;
import cz.landspa.statsapp2.model.entity.Game;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GameService {
    Game createGame(Game game);

    List<Game> getTeamGames(Long teamId);

    Optional<Game> getGameById(Long id);

    GameFullInfoDTO getGameWithFullInfoById(Long id);

    void deleteGame(Long id);

    Game updateGame(Game game, Long id);

    Long getNumberOfGamesByUser(Long userId);

    List<Game> get4MostRecentGames(Long userId);

    List<Game> get4NextGames(Long userId);

    void addPeriod(Long id);

    void removePeriod(Long id);

}
