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

    @PostMapping(value = "results")
    public String processSearch(Model model, @RequestParam String searchTerm, @RequestParam String searchType) {
        List<HashMap<String, String>> searchResults = new ArrayList<>();

        for (Job job : jobs) {
            if ("all".equals(searchType) || jobFieldContainsSearchTerm(job, searchType, searchTerm)) {
                HashMap<String, String> jobMap = new HashMap<>();
                jobMap.put("id", "job-" + idCounter);
                jobMap.put("employer", job.getEmployer().getValue());
                jobMap.put("location", job.getLocation().getValue());
                jobMap.put("positionType", job.getPositionType().getValue());
                jobMap.put("coreCompetency", job.getCoreCompetency().getValue());
                searchResults.add(jobMap);
                idCounter++;
            }
        }


        //Add the search results to the model to be accessed in the view
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("columns", columnChoices);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("searchType", searchType);

        return "search";
    }

    private boolean jobFieldContainsSearchTerm(Job job, String searchType, String searchTerm) {
        switch (searchType) {
            case "employer":
                return job.getEmployer().getValue().toLowerCase().contains(searchTerm.toLowerCase());
            case "location":
                return job.getLocation().getValue().toLowerCase().contains(searchTerm.toLowerCase());
            case "positionType":
                return job.getPositionType().getValue().toLowerCase().contains(searchTerm.toLowerCase());
            case "coreCompetency":
                return job.getCoreCompetency().getValue().toLowerCase().contains(searchTerm.toLowerCase());
            default:
                return false;
        }
    }
}
