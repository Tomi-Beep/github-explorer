package mk.ukim.finki.wp.githubexplorer.service;

import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubReadmeDto;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface GitHubRepositoryService {

    Page<GitHubRepositoryDto> searchRepositories(
            String keyword,
            String language,
            String topic,
            Integer minStars,
            Integer pageNum,
            Integer pageSize
    );

    GitHubRepositoryDto getRepository(String owner, String name);

    Map<String, Long> getRepositoryLanguages(String owner, String name);

    GitHubReadmeDto getRepositoryReadme(String owner, String name);
}