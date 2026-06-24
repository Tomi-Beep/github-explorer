package mk.ukim.finki.wp.githubexplorer.service.impl;

import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubSearchResponseDto;
import mk.ukim.finki.wp.githubexplorer.service.GitHubRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class GitHubRepositoryServiceImpl implements GitHubRepositoryService {

    private final RestClient gitHubRestClient;

    public GitHubRepositoryServiceImpl(RestClient gitHubRestClient) {
        this.gitHubRestClient = gitHubRestClient;
    }

    @Override
    public List<GitHubRepositoryDto> searchRepositories(String keyword) {
        try {
            GitHubSearchResponseDto response = gitHubRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", keyword)
                            .queryParam("per_page", 20)
                            .build())
                    .retrieve()
                    .body(GitHubSearchResponseDto.class);

            if (response == null || response.getItems() == null) {
                return List.of();
            }

            return response.getItems();
        } catch (RestClientException e) {
            throw new IllegalStateException("GitHub API is currently unavailable. Please try again later.");
        }
    }

    @Override
    public GitHubRepositoryDto getRepository(String owner, String name) {
        try {
            return gitHubRestClient.get()
                    .uri("/repos/{owner}/{name}", owner, name)
                    .retrieve()
                    .body(GitHubRepositoryDto.class);
        } catch (RestClientException e) {
            throw new IllegalStateException("Repository details could not be loaded from GitHub.");
        }
    }
}
