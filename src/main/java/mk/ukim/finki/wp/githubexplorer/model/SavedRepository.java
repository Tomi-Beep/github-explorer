package mk.ukim.finki.wp.githubexplorer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
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

    private Long forks;

    @Column(length = 1000)
    private String htmlUrl;

    private Long openIssuesCount;

    private String updatedAt;

    @Column(length = 1000)
    private String note;

    private String tag;

    public SavedRepository(
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
        this.githubId = githubId;
        this.name = name;
        this.fullName = fullName;
        this.ownerLogin = ownerLogin;
        this.description = description;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
        this.htmlUrl = htmlUrl;
        this.openIssuesCount = openIssuesCount;
        this.updatedAt = updatedAt;
        this.note = "";
        this.tag = "";
    }
}
