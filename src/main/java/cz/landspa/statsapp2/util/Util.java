package cz.landspa.statsapp2.util;

import cz.landspa.statsapp2.model.entity.Game;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Util {

    public Util() {
    }

    public static Map<String,LocalDate> getSeasonDates() {
        Map<String, LocalDate> dates = new HashMap<>(2);
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() >= 1 && now.getMonthValue() <= 5) {
            dates.put("startDate", LocalDate.of(now.getYear() - 1, 6, 1));
            dates.put("endDate", LocalDate.of(now.getYear(), 5, 31));
        } else {
            dates.put("startDate", LocalDate.of(now.getYear(), 6, 1));
            dates.put("endDate", LocalDate.of(now.getYear()+1, 5, 31));

        }

        return dates;
    }

    public static String getUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        return scheme + "://" + serverName + (serverPort != 80 && serverPort != 443 ? ":" + serverPort : "");
    }


    public static Pageable createPageableFromDataTableParams(Map<String, String> params, String[] columns) {
        int start = Integer.parseInt(params.getOrDefault("start", "0"));
        int length = Integer.parseInt(params.getOrDefault("length", "10"));

        int orderColumn = Integer.parseInt(params.getOrDefault("order[0][column]", "0"));
        String orderDir = params.getOrDefault("order[0][dir]", "asc");

        String sortBy = columns.length > orderColumn ? columns[orderColumn] : columns[0];
        Sort.Direction direction = orderDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        int page = start / length;

        return PageRequest.of(page, length, Sort.by(direction, sortBy));

    }



    public static Map<Integer, PeriodTime> getStartAndEndPeriodTimes(Game game) {
        Map<Integer, PeriodTime> periods = new HashMap<>();

        int periodLengthSeconds = game.getPeriodLength() * 60;

        for (int i = 1; i <= game.getPeriods(); i++) {
            int startTime = (i - 1) * periodLengthSeconds;
            if (i > 1) startTime += 1;
            int endTime = i * periodLengthSeconds;

            periods.put(i, new PeriodTime(startTime, endTime));
        }

        int overtimeStart = game.getPeriods() * periodLengthSeconds + 1;
        periods.put(game.getPeriods() + 1, new PeriodTime(overtimeStart, null));

        return periods;
    }




}

