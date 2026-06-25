package mk.ukim.finki.wp.githubexplorer.web.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.service.GitHubRepositoryService;
import mk.ukim.finki.wp.githubexplorer.service.SavedRepositoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String searchRepositories(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;

        model.addAttribute("keyword", keyword);
        model.addAttribute("language", language);
        model.addAttribute("topic", topic);
        model.addAttribute("minStars", minStars);
        model.addAttribute("pageSize", safePageSize);

        if (isSearchEmpty(keyword, language, topic, minStars)) {
            model.addAttribute("error", "Please enter a keyword or at least one filter.");
            model.addAttribute("page", Page.empty(PageRequest.of(safePageNum - 1, safePageSize)));
        } else {
            try {
                model.addAttribute("page", gitHubRepositoryService.searchRepositories(
                        keyword,
                        language,
                        topic,
                        minStars,
                        safePageNum - 1,
                        safePageSize
                ));
            } catch (IllegalStateException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("page", Page.empty(PageRequest.of(safePageNum - 1, safePageSize)));
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
            model.addAttribute("languages", gitHubRepositoryService.getRepositoryLanguages(owner, name));
            model.addAttribute("readme", gitHubRepositoryService.getRepositoryReadme(owner, name));
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("bodyContent", "repository-details");
        return "master-template";
    }

    @PostMapping("/save/{owner}/{name}")
    public String saveRepository(
            @PathVariable String owner,
            @PathVariable String name,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        GitHubRepositoryDto repository = gitHubRepositoryService.getRepository(owner, name);
        savedRepositoryService.save(repository, userId);
        return "redirect:/watchlist";
    }

    private boolean isSearchEmpty(String keyword, String language, String topic, Integer minStars) {
        return (keyword == null || keyword.isBlank())
                && (language == null || language.isBlank())
                && (topic == null || topic.isBlank())
                && (minStars == null || minStars <= 0);
    }
}