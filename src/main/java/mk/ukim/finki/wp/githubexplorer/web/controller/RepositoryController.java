package mk.ukim.finki.wp.githubexplorer.web.controller;

import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.service.GitHubRepositoryService;
import mk.ukim.finki.wp.githubexplorer.service.SavedRepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/repositories")
public class RepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;
    private final SavedRepositoryService savedRepositoryService;

    public RepositoryController(
            GitHubRepositoryService gitHubRepositoryService,
            SavedRepositoryService savedRepositoryService
    ) {
        this.gitHubRepositoryService = gitHubRepositoryService;
        this.savedRepositoryService = savedRepositoryService;
    }

    @GetMapping
    public String searchRepositories(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("repositories", List.of());

        if (keyword == null || keyword.isBlank()) {
            model.addAttribute("error", "Please enter a keyword.");
        } else {
            try {
                model.addAttribute("repositories", gitHubRepositoryService.searchRepositories(keyword.trim()));
            } catch (IllegalStateException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        model.addAttribute("bodyContent", "repository-results");
        return "master-template";
    }

    @GetMapping("/{owner}/{name}")
    public String getRepositoryDetails(
            @PathVariable String owner,
            @PathVariable String name,
            Model model
    ) {
        try {
            model.addAttribute("repository", gitHubRepositoryService.getRepository(owner, name));
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("bodyContent", "repository-details");
        return "master-template";
    }

    @PostMapping("/save/{owner}/{name}")
    public String saveRepository(@PathVariable String owner, @PathVariable String name) {
        GitHubRepositoryDto repository = gitHubRepositoryService.getRepository(owner, name);
        savedRepositoryService.save(repository);
        return "redirect:/watchlist";
    }
}
