package mk.ukim.finki.wp.githubexplorer.web.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.wp.githubexplorer.model.AppUser;
import mk.ukim.finki.wp.githubexplorer.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String redirect,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("redirect", redirect);
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String redirect,
            HttpSession session,
            Model model
    ) {
        try {
            AppUser user = userService.login(username, password);
            session.setAttribute("currentUserId", user.getId());
            session.setAttribute("currentUsername", user.getUsername());
            return redirectAfterAuthentication(redirect);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("redirect", redirect);
            model.addAttribute("bodyContent", "login");
            return "master-template";
        }
    }

    @GetMapping("/register")
    public String getRegisterPage(
            @RequestParam(required = false) String redirect,
            Model model
    ) {
        model.addAttribute("redirect", redirect);
        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String repeatedPassword,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String redirect,
            HttpSession session,
            Model model
    ) {
        try {
            AppUser user = userService.register(username, password, repeatedPassword, name);
            session.setAttribute("currentUserId", user.getId());
            session.setAttribute("currentUsername", user.getUsername());
            return redirectAfterAuthentication(redirect);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("name", name);
            model.addAttribute("redirect", redirect);
            model.addAttribute("bodyContent", "register");
            return "master-template";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private String redirectAfterAuthentication(String redirect) {
        if (redirect == null || redirect.isBlank() || !redirect.startsWith("/") || redirect.startsWith("//")) {
            return "redirect:/watchlist";
        }
        return "redirect:" + redirect;
    }
}