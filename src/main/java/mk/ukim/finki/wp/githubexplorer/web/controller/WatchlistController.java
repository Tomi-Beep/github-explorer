package mk.ukim.finki.wp.githubexplorer.web.controller;

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

    public WatchlistController(SavedRepositoryService savedRepositoryService) {
        this.savedRepositoryService = savedRepositoryService;
    }

    @GetMapping
    public String getWatchlistPage(Model model) {
        model.addAttribute("repositories", savedRepositoryService.listSavedRepositories());
        model.addAttribute("bodyContent", "watchlist");
        return "master-template";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("repository", savedRepositoryService.findById(id));
        model.addAttribute("bodyContent", "watchlist-form");
        return "master-template";
    }

    @PostMapping("/edit/{id}")
    public String updateRepository(
            @PathVariable Long id,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String tag
    ) {
        savedRepositoryService.update(id, note, tag);
        return "redirect:/watchlist";
    }

    @PostMapping("/delete/{id}")
    public String deleteRepository(@PathVariable Long id) {
        savedRepositoryService.delete(id);
        return "redirect:/watchlist";
    }
}
