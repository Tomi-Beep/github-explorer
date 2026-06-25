package mk.ukim.finki.wp.githubexplorer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("pushed_at")
    private String pushedAt;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("watchers_count")
    private Long watchers;

    @JsonProperty("subscribers_count")
    private Long subscribers;

    private String homepage;
    private Long size;
    private Boolean fork;
    private Boolean archived;
    private String visibility;
    private GitHubLicenseDto license;
    private List<String> topics = new ArrayList<>();
}