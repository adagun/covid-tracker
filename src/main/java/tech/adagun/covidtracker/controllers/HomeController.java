package tech.adagun.covidtracker.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tech.adagun.covidtracker.models.RegionStats;
import tech.adagun.covidtracker.services.CovidDataService;

import java.util.List;

@Controller
public class HomeController
{
    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String home(Model model)
    {

        List<RegionStats> statsList = covidDataService.getStatsList();
                                                        // method reference
        int totalCases = statsList.stream().mapToInt(RegionStats::getLatestTotalCases).sum();
        int totalNewCases = statsList.stream().mapToInt(RegionStats::getCasesDiffPreviousDay).sum();
        int totalNewDeaths = statsList.stream().mapToInt(RegionStats::getDeathsDiffPreviousDay).sum();// lambda
        int totalDeaths = statsList.stream().mapToInt(RegionStats::getLatestTotalDeaths).sum();

        model.addAttribute("regionStats", statsList);
        model.addAttribute("totalCases", totalCases);
        model.addAttribute("totalDeaths", totalDeaths);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("totalNewDeaths", totalNewDeaths);
        return "home";
    }
}