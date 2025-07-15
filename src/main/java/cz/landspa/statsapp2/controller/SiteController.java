package cz.landspa.statsapp2.controller;


import cz.landspa.statsapp2.model.DTO.game.GameFullInfoDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Team;
import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.model.entity.user.UserSetting;
import cz.landspa.statsapp2.service.GameService;
import cz.landspa.statsapp2.service.StatsService;
import cz.landspa.statsapp2.service.TeamService;
import cz.landspa.statsapp2.service.UserService;
import cz.landspa.statsapp2.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SiteController {

    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final TeamService teamService;
    private final GameService gameService;
    private final StatsService statsService;

    public SiteController(UserService userService, SecurityUtil securityUtil, TeamService teamService, GameService gameService, StatsService statsService) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.teamService = teamService;
        this.gameService = gameService;
        this.statsService = statsService;
    }

    @GetMapping
    public String index(Model model){
            User user = securityUtil.getCurrentUser();
            UserSetting userSetting = user.getUserSetting();
            model.addAttribute("name", user.getUsername());
            model.addAttribute("periods", userSetting.getDefaultPeriods());
            model.addAttribute("periodLength", userSetting.getDefaultPeriodLength());
            return "index";

    }

    @GetMapping("teams")
    public String myTeams(Model model){
            User user = securityUtil.getCurrentUser();
            model.addAttribute("name", user.getUsername());
            return "myteams";

    }

    @GetMapping("teams/{id}")
    public String teamDetail(Model model, @PathVariable Long id){
        User user = securityUtil.getCurrentUser();
        model.addAttribute("name", user.getUsername());
        Team team = teamService.getTeamById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Tým nebyl nalezen"));
        return "teamDetail";

    }

    @GetMapping("games/{id}")
    public String gameRoster(Model model, @PathVariable Long id){
            User user = securityUtil.getCurrentUser();
            model.addAttribute("name", user.getUsername());
            Game game = gameService.getGameById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Zápas nebyl nalezen"));
            model.addAttribute("teamId", game.getTeam().getId());
            return "pregame";
    }

    @GetMapping("games/{id}/record")
    public String statsRecord(Model model, @PathVariable Long id){
            User user = securityUtil.getCurrentUser();
            model.addAttribute("name", user.getUsername());
            Game game = gameService.getGameById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Zápas nebyl nalezen"));
            model.addAttribute("id", id);
                return "record";

    }

    @GetMapping("games/{id}/summary")
    public String summary(Model model, @PathVariable Long id){
        GameFullInfoDTO game = gameService.getGameWithFullInfoById(id);

                model.addAttribute("playerStats", statsService.getGamePlayerStats(id));
                model.addAttribute("game", game);
                model.addAttribute("gameStats", statsService.getGameTeamStats(id));
                return "summary";


    }




    @GetMapping("stats")
    public String stats(Model model){
            User user = securityUtil.getCurrentUser();
            model.addAttribute("name", user.getUsername());
            return "stats";


    }

    @GetMapping("settings")
    public String settingsPage(Model model) {
        User user = securityUtil.getCurrentUser();
        model.addAttribute("name", user.getUsername());
        return "settings";
   }

    @GetMapping("help")
    public String supportPage(Model model) {
        User user = securityUtil.getCurrentUser();
        model.addAttribute("name", user.getUsername());
        return "support";
   }

    @GetMapping("activate")
    public String activateAccount(@RequestParam("token") String token){
        try {
            userService.activateUser(token);

            return "validToken";
        } catch (Exception e){
            return "invalidToken";
        }
    }

    @GetMapping("login")
    public String loginPage()
    {return "login";}

    @GetMapping("register")
    public String registerPage()
    {return "register";}


    @GetMapping("test")
    public String testPage(){return "test";}
}
