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
public class GitHubSearchResponseDto {

    @JsonProperty("total_count")
    private Integer totalCount;

    private List<GitHubRepositoryDto> items = new ArrayList<>();
}
