package cz.landspa.statsapp2.util;

import cz.landspa.statsapp2.model.DTO.stats.game.PeriodGoals;
import cz.landspa.statsapp2.model.entity.Roster;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller("util")
public class SummaryUtil {
    public String periodWithName(int current, int total) {
        if (current == 0) return "Před zápasem";
        if (current <= total) {
            return current + ". " + getPeriodName(total);
        }
        return "Prodloužení";
    }

    private String getPeriodName(int total) {
        return switch (total) {
            case 2 -> "poločas";
            case 3 -> "třetina";
            case 4 -> "čtvrtina";
            default -> "perioda";
        };
    }

    public static String secondsToGameTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String penaltyMinutesToText(int minutes) {
        return switch (minutes) {
            case 2 -> "2 min";
            case 4 -> "2+2 min";
            case 12 -> "2+10 min";
            case 10 -> "10 min";
            case 5 -> "5 min";
            case 25 -> "5 min + OK";
            case 20 -> "OK";
            case 0 -> "TS";
            default -> minutes + " min";
        };
    }

    public String joinGameNumbersFromRoster(List<Roster> rosterList) {
        if (rosterList == null || rosterList.isEmpty()) {
            return "--";
        }
        return rosterList.stream()
                .map(r -> r.getGameNumber() != null ? r.getGameNumber().toString() : "--")
                .collect(Collectors.joining(", "));
    }

    public String periodScoreToText(List<PeriodGoals> goals, Integer periods){
        return goals.stream()
                .filter(p -> p.getPeriod() <= periods)
                .map(p-> p.getGoalsScored()+":"+p.getGoalsConceded())
                .collect(Collectors.joining(", "));


    }
}
