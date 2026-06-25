package mk.ukim.finki.wp.githubexplorer.service;

import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.model.enums.RepositoryCategory;

import java.util.List;

public interface SavedRepositoryService {

    List<SavedRepository> listSavedRepositories(Long userId, String tag);

    SavedRepository findById(Long id, Long userId);

    SavedRepository save(GitHubRepositoryDto repository, Long userId);

    SavedRepository update(Long id, Long userId, String note, String tag, RepositoryCategory category);

    SavedRepository refreshGitHubData(Long id, Long userId, GitHubRepositoryDto repository);

    void delete(Long id, Long userId);
}