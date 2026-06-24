package mk.ukim.finki.wp.githubexplorer.service.impl;

import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.repository.SavedRepositoryRepository;
import mk.ukim.finki.wp.githubexplorer.service.SavedRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedRepositoryServiceImpl implements SavedRepositoryService {

    private final SavedRepositoryRepository savedRepositoryRepository;

    public SavedRepositoryServiceImpl(SavedRepositoryRepository savedRepositoryRepository) {
        this.savedRepositoryRepository = savedRepositoryRepository;
    }

    @Override
    public List<SavedRepository> listSavedRepositories() {
        return savedRepositoryRepository.findAll();
    }

    @Override
    public SavedRepository findById(Long id) {
        return savedRepositoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Saved repository was not found."));
    }

    @Override
    public SavedRepository save(GitHubRepositoryDto repository) {
        return savedRepositoryRepository.findByGithubId(repository.getId())
                .orElseGet(() -> savedRepositoryRepository.save(new SavedRepository(
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
    public SavedRepository update(Long id, String note, String tag) {
        SavedRepository repository = findById(id);
        repository.setNote(note == null ? "" : note);
        repository.setTag(tag == null ? "" : tag);
        return savedRepositoryRepository.save(repository);
    }

    @Override
    public void delete(Long id) {
        savedRepositoryRepository.deleteById(id);
    }
}
