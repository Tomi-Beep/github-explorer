package mk.ukim.finki.wp.githubexplorer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.githubexplorer.model.enums.RepositoryCategory;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "saved_repository",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "github_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "github_id", nullable = false)
    private Long githubId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fullName;

    private String ownerLogin;

    @Column(length = 2000)
    private String description;

    private String language;

    private Long stars;

    private Long starsWhenSaved;

    private Long forks;

    @Column(length = 1000)
    private String htmlUrl;

    private Long openIssuesCount;

    private String updatedAt;

    private LocalDateTime savedAt;

    @Column(length = 1000)
    private String note;

    private String tag;

    @Enumerated(EnumType.STRING)
    private RepositoryCategory category;

    public SavedRepository(
            AppUser user,
            Long githubId,
            String name,
            String fullName,
            String ownerLogin,
            String description,
            String language,
            Long stars,
            Long forks,
            String htmlUrl,
            Long openIssuesCount,
            String updatedAt
    ) {
        this.user = user;
        this.githubId = githubId;
        this.name = name;
        this.fullName = fullName;
        this.ownerLogin = ownerLogin;
        this.description = description;
        this.language = language;
        this.stars = stars;
        this.starsWhenSaved = stars;
        this.forks = forks;
        this.htmlUrl = htmlUrl;
        this.openIssuesCount = openIssuesCount;
        this.updatedAt = updatedAt;
        this.savedAt = LocalDateTime.now();
        this.note = "";
        this.tag = "";
        this.category = RepositoryCategory.TO_STUDY;
    }

    public Long getStarsDifference() {
        Long currentStars = stars == null ? 0L : stars;
        Long savedStars = starsWhenSaved == null ? 0L : starsWhenSaved;
        return currentStars - savedStars;
    }
}