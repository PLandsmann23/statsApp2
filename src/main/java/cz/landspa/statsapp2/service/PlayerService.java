package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerService {

    Player createPlayer(Player player);

    List<Player> getTeamPlayers(Long teamId);

    Player getPlayerById(Long id);

    void deletePlayer(Long id);


    Player updatePlayer(Player player, Long id);


    Long getNumberOfPlayersByUser(Long userId);

    List<Player> findTeamPlayersNotInRoster(Long gameId);

}
