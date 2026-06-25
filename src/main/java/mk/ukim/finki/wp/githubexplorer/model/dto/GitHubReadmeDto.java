package mk.ukim.finki.wp.githubexplorer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubReadmeDto {

    private String name;
    private String path;
    private String content;
    private String encoding;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("download_url")
    private String downloadUrl;

    private String text;
    private String html;
    private boolean truncated;
}