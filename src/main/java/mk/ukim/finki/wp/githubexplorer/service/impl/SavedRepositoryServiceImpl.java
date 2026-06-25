package mk.ukim.finki.wp.githubexplorer.service.impl;

import mk.ukim.finki.wp.githubexplorer.model.AppUser;
import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.model.enums.RepositoryCategory;
import mk.ukim.finki.wp.githubexplorer.repository.AppUserRepository;
import mk.ukim.finki.wp.githubexplorer.repository.SavedRepositoryRepository;
import mk.ukim.finki.wp.githubexplorer.service.SavedRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedRepositoryServiceImpl implements SavedRepositoryService {

    private final SavedRepositoryRepository savedRepositoryRepository;
    private final AppUserRepository appUserRepository;

    public SavedRepositoryServiceImpl(
            SavedRepositoryRepository savedRepositoryRepository,
            AppUserRepository appUserRepository
    ) {
        this.savedRepositoryRepository = savedRepositoryRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<SavedRepository> listSavedRepositories(Long userId, String tag) {
        if (tag != null && !tag.isBlank()) {
            return savedRepositoryRepository.findAllByUser_IdAndTagContainingIgnoreCaseOrderBySavedAtDesc(userId, tag.trim());
        }
        return savedRepositoryRepository.findAllByUser_IdOrderBySavedAtDesc(userId);
    }

    @Override
    public SavedRepository findById(Long id, Long userId) {
        return savedRepositoryRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Saved repository was not found."));
    }

    @Override
    public SavedRepository save(GitHubRepositoryDto repository, Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User was not found."));

        return savedRepositoryRepository.findByUser_IdAndGithubId(userId, repository.getId())
                .orElseGet(() -> savedRepositoryRepository.save(new SavedRepository(
                        user,
                        repository.getId(),
                        repository.getName(),
                        repository.getFullName(),
                        repository.getOwner() == null ? "" : repository.getOwner().getLogin(),
                        repository.getDescription(),
                        repository.getLanguage(),
                        repository.getStars() == null ? 0L : repository.getStars(),
                        repository.getForks() == null ? 0L : repository.getForks(),
                        repository.getHtmlUrl(),
                        repository.getOpenIssuesCount() == null ? 0L : repository.getOpenIssuesCount(),
                        repository.getUpdatedAt()
                )));
    }

    @Override
    public SavedRepository update(Long id, Long userId, String note, String tag, RepositoryCategory category) {
        SavedRepository repository = findById(id, userId);
        repository.setNote(note == null ? "" : note);
        repository.setTag(tag == null ? "" : tag);
        repository.setCategory(category == null ? RepositoryCategory.TO_STUDY : category);
        return savedRepositoryRepository.save(repository);
    }

    @Override
    public SavedRepository refreshGitHubData(Long id, Long userId, GitHubRepositoryDto repositoryDto) {
        SavedRepository repository = findById(id, userId);
        repository.setDescription(repositoryDto.getDescription());
        repository.setLanguage(repositoryDto.getLanguage());
        repository.setStars(repositoryDto.getStars() == null ? 0L : repositoryDto.getStars());
        repository.setForks(repositoryDto.getForks() == null ? 0L : repositoryDto.getForks());
        repository.setHtmlUrl(repositoryDto.getHtmlUrl());
        repository.setOpenIssuesCount(repositoryDto.getOpenIssuesCount() == null ? 0L : repositoryDto.getOpenIssuesCount());
        repository.setUpdatedAt(repositoryDto.getUpdatedAt());
        return savedRepositoryRepository.save(repository);
    }

    @Override
    public void delete(Long id, Long userId) {
        SavedRepository repository = findById(id, userId);
        savedRepositoryRepository.delete(repository);
    }
}