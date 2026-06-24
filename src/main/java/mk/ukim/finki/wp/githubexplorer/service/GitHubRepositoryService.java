package mk.ukim.finki.wp.githubexplorer.service;

import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;

import java.util.List;

public interface GitHubRepositoryService {

    List<GitHubRepositoryDto> searchRepositories(String keyword);

    GitHubRepositoryDto getRepository(String owner, String name);
}
