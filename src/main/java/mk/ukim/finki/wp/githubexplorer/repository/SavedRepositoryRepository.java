package mk.ukim.finki.wp.githubexplorer.repository;

import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedRepositoryRepository extends JpaRepository<SavedRepository, Long> {

    Optional<SavedRepository> findByGithubId(Long githubId);
}
