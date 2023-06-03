package org.launchcode.techjobs.mvc.controllers;

import org.launchcode.techjobs.mvc.models.Job;
import org.launchcode.techjobs.mvc.models.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("search")
public class SearchController {

    // TODO #3 - Create a handler to process a search request and render the updated search view.
    private HashMap<String, String> columnChoices = new HashMap<>();
    private List<Job> jobs;
    private int idCounter = 1;

    public SearchController() {
        columnChoices.put("all", "All");
        columnChoices.put("name", "Name");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("positionType", "Position Type");
        columnChoices.put("coreCompetency", "Skill");
        jobs = JobData.findAll();
    }

    @GetMapping(value = "")
    public String search(Model model) {
        model.addAttribute("columns", columnChoices);
        return "search";
    }


    // TODO: Impliment the search logic based on the searchTerm and its parameters.

    @PostMapping(value = "/results")
    public String displaySearchResults(Model model, @RequestParam String searchTerm, @RequestParam String searchType) {
        List<Job> searchResults;
        if ("all".equals(searchType) || searchTerm.isEmpty()) {
            searchResults = JobData.findAll();
        } else {
            searchResults = JobData.findByColumnAndValue(searchType, searchTerm);
        }
        List<HashMap<String, String>> formattedResults = formatSearchResults(searchResults);

        model.addAttribute("searchResults", formattedResults);
        model.addAttribute("columns", columnChoices);
        idCounter = 1;
        return "search";
    }

    private List<HashMap<String, String>> formatSearchResults(List<Job> searchResults) {
        List<HashMap<String, String>> formattedResults = new ArrayList<>();

        for (Job job : searchResults) {
            HashMap<String, String> jobMap = new HashMap<>();
            jobMap.put("id", String.valueOf(idCounter));
            jobMap.put("name", job.getName());
            jobMap.put("employer", job.getEmployer().getValue());
            jobMap.put("location", job.getLocation().getValue());
            jobMap.put("positionType", job.getPositionType().getValue());
            jobMap.put("coreCompetency", job.getCoreCompetency().getValue());
            formattedResults.add(jobMap);
            idCounter++;
        }

        return formattedResults;
    }
}