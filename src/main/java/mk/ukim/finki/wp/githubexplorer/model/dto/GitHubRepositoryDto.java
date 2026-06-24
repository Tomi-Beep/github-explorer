package mk.ukim.finki.wp.githubexplorer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryDto {

    private Long id;
    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private GitHubOwnerDto owner;
    private String description;
    private String language;

    @JsonProperty("stargazers_count")
    private Long stars;

    @JsonProperty("forks_count")
    private Long forks;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("open_issues_count")
    private Long openIssuesCount;

    @JsonProperty("updated_at")
    private String updatedAt;
}
