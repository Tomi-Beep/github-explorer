package mk.ukim.finki.wp.githubexplorer.repository;

import mk.ukim.finki.wp.githubexplorer.model.SavedRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedRepositoryRepository extends JpaRepository<SavedRepository, Long> {

    List<SavedRepository> findAllByUser_IdOrderBySavedAtDesc(Long userId);

    List<SavedRepository> findAllByUser_IdAndTagContainingIgnoreCaseOrderBySavedAtDesc(Long userId, String tag);

    Optional<SavedRepository> findByIdAndUser_Id(Long id, Long userId);

    Optional<SavedRepository> findByUser_IdAndGithubId(Long userId, Long githubId);
}