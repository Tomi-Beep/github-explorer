package mk.ukim.finki.wp.githubexplorer.service;

import mk.ukim.finki.wp.githubexplorer.model.AppUser;

public interface UserService {

    AppUser register(String username, String password, String repeatedPassword, String name);

    AppUser login(String username, String password);

    AppUser findById(Long id);
}