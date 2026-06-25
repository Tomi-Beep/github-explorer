package mk.ukim.finki.wp.githubexplorer.service.impl;

import mk.ukim.finki.wp.githubexplorer.model.AppUser;
import mk.ukim.finki.wp.githubexplorer.repository.AppUserRepository;
import mk.ukim.finki.wp.githubexplorer.service.UserService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;

    public UserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser register(String username, String password, String repeatedPassword, String name) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (!password.equals(repeatedPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        String cleanUsername = username.trim();
        if (appUserRepository.findByUsername(cleanUsername).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        String displayName = name == null || name.isBlank() ? cleanUsername : name.trim();
        return appUserRepository.save(new AppUser(cleanUsername, hashPassword(password), displayName));
    }

    @Override
    public AppUser login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        AppUser user = appUserRepository.findByUsername(username.trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!user.getPassword().equals(hashPassword(password))) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        return user;
    }

    @Override
    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User was not found."));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Password hashing is not available.");
        }
    }
}