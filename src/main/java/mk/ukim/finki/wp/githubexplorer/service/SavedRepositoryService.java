package mk.ukim.finki.wp.githubexplorer.service;

import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;

import java.util.List;

public interface SavedRepositoryService {

    List<SavedRepository> listSavedRepositories();

    SavedRepository findById(Long id);

    SavedRepository save(GitHubRepositoryDto repository);

    SavedRepository update(Long id, String note, String tag);

    void delete(Long id);
}
