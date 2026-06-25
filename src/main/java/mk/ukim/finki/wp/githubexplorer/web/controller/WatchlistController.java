package mk.ukim.finki.wp.githubexplorer.web.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.model.enums.RepositoryCategory;
import mk.ukim.finki.wp.githubexplorer.service.GitHubRepositoryService;
import mk.ukim.finki.wp.githubexplorer.service.SavedRepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {

    private final SavedRepositoryService savedRepositoryService;
    private final GitHubRepositoryService gitHubRepositoryService;

    public WatchlistController(
            SavedRepositoryService savedRepositoryService,
            GitHubRepositoryService gitHubRepositoryService
    ) {
        this.savedRepositoryService = savedRepositoryService;
        this.gitHubRepositoryService = gitHubRepositoryService;
    }

    @GetMapping
    public String getWatchlistPage(
            @RequestParam(required = false) String tag,
            HttpSession session,
            Model model
    ) {
        Long userId = currentUserId(session);
        model.addAttribute("repositories", savedRepositoryService.listSavedRepositories(userId, tag));
        model.addAttribute("tag", tag);
        model.addAttribute("bodyContent", "watchlist");
        return "master-template";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable Long id, HttpSession session, Model model) {
        model.addAttribute("repository", savedRepositoryService.findById(id, currentUserId(session)));
        model.addAttribute("categories", RepositoryCategory.values());
        model.addAttribute("bodyContent", "watchlist-form");
        return "master-template";
    }

    @PostMapping("/edit/{id}")
    public String updateRepository(
            @PathVariable Long id,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) RepositoryCategory category,
            HttpSession session
    ) {
        savedRepositoryService.update(id, currentUserId(session), note, tag, category);
        return "redirect:/watchlist";
    }

    @PostMapping("/refresh/{id}")
    public String refreshRepository(@PathVariable Long id, HttpSession session) {
        Long userId = currentUserId(session);
        SavedRepository savedRepository = savedRepositoryService.findById(id, userId);
        GitHubRepositoryDto repositoryDto = gitHubRepositoryService.getRepository(
                savedRepository.getOwnerLogin(),
                savedRepository.getName()
        );
        savedRepositoryService.refreshGitHubData(id, userId, repositoryDto);
        return "redirect:/watchlist";
    }

    @PostMapping("/delete/{id}")
    public String deleteRepository(@PathVariable Long id, HttpSession session) {
        savedRepositoryService.delete(id, currentUserId(session));
        return "redirect:/watchlist";
    }

    private Long currentUserId(HttpSession session) {
        return (Long) session.getAttribute("currentUserId");
    }
}