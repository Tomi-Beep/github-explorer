package mk.ukim.finki.wp.githubexplorer.service.impl;

import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubReadmeDto;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubRepositoryDto;
import mk.ukim.finki.wp.githubexplorer.model.dto.GitHubSearchResponseDto;
import mk.ukim.finki.wp.githubexplorer.service.GitHubRepositoryService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class GitHubRepositoryServiceImpl implements GitHubRepositoryService {

    private final RestClient gitHubRestClient;

    public GitHubRepositoryServiceImpl(RestClient gitHubRestClient) {
        this.gitHubRestClient = gitHubRestClient;
    }

    @Override
    public Page<GitHubRepositoryDto> searchRepositories(
            String keyword,
            String language,
            String topic,
            Integer minStars,
            Integer pageNum,
            Integer pageSize
    ) {
        int safePageNum = pageNum == null || pageNum < 0 ? 0 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 50);
        Pageable pageable = PageRequest.of(safePageNum, safePageSize);
        String query = buildSearchQuery(keyword, language, topic, minStars);

        if (query.isBlank()) {
            return Page.empty(pageable);
        }

        try {
            GitHubSearchResponseDto response = gitHubRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", query)
                            .queryParam("page", safePageNum + 1)
                            .queryParam("per_page", safePageSize)
                            .build())
                    .retrieve()
                    .body(GitHubSearchResponseDto.class);

            if (response == null || response.getItems() == null) {
                return Page.empty(pageable);
            }

            long totalResults = response.getTotalCount() == null ? 0 : Math.min(response.getTotalCount(), 1000);
            return new PageImpl<>(response.getItems(), pageable, totalResults);
        } catch (RestClientException e) {
            throw new IllegalStateException("GitHub API is currently unavailable. Please try again later.");
        }
    }

    @Override
    public GitHubRepositoryDto getRepository(String owner, String name) {
        try {
            return gitHubRestClient.get()
                    .uri("/repos/{owner}/{name}", owner, name)
                    .retrieve()
                    .body(GitHubRepositoryDto.class);
        } catch (RestClientException e) {
            throw new IllegalStateException("Repository details could not be loaded from GitHub.");
        }
    }

    @Override
    public Map<String, Long> getRepositoryLanguages(String owner, String name) {
        try {
            Map<String, Long> languages = gitHubRestClient.get()
                    .uri("/repos/{owner}/{name}/languages", owner, name)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Long>>() {});

            return languages == null ? Map.of() : languages;
        } catch (RestClientException e) {
            return Map.of();
        }
    }

    @Override
    public GitHubReadmeDto getRepositoryReadme(String owner, String name) {
        try {
            GitHubReadmeDto readme = gitHubRestClient.get()
                    .uri("/repos/{owner}/{name}/readme", owner, name)
                    .retrieve()
                    .body(GitHubReadmeDto.class);

            return prepareReadme(readme, owner, name);
        } catch (RestClientException e) {
            return null;
        }
    }

    private GitHubReadmeDto prepareReadme(GitHubReadmeDto readme, String owner, String name) {
        if (readme == null || readme.getContent() == null || readme.getContent().isBlank()) {
            return null;
        }

        if (!"base64".equalsIgnoreCase(readme.getEncoding())) {
            readme.setText("README content is available on GitHub, but this app could not decode it.");
            readme.setHtml(readmeFallbackHtml());
            return readme;
        }

        String decoded;
        try {
            decoded = new String(
                    Base64.getMimeDecoder().decode(readme.getContent()),
                    StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException e) {
            readme.setText("README content is available on GitHub, but this app could not decode it.");
            readme.setHtml(readmeFallbackHtml());
            return readme;
        }

        readme.setText(decoded);
        readme.setHtml(renderMarkdown(decoded, owner, name));
        return readme;
    }

    private String renderMarkdown(String markdown, String owner, String name) {
        try {
            String html = gitHubRestClient.post()
                    .uri("/markdown")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_HTML)
                    .body(Map.of(
                            "text", markdown,
                            "mode", "gfm",
                            "context", owner + "/" + name
                    ))
                    .retrieve()
                    .body(String.class);

            if (html == null || html.isBlank()) {
                return readmeFallbackHtml();
            }

            return html;
        } catch (RestClientException e) {
            return readmeFallbackHtml();
        }
    }

    private String readmeFallbackHtml() {
        return "<p class=\"text-secondary mb-0\">README content is available on GitHub.</p>";
    }

    private String buildSearchQuery(String keyword, String language, String topic, Integer minStars) {
        StringJoiner query = new StringJoiner(" ");

        if (keyword != null && !keyword.isBlank()) {
            query.add(keyword.trim());
        }

        if (language != null && !language.isBlank()) {
            query.add("language:" + language.trim());
        }

        if (topic != null && !topic.isBlank()) {
            query.add("topic:" + topic.trim());
        }

        if (minStars != null && minStars > 0) {
            query.add("stars:>=" + minStars);
        }

        return query.toString();
    }
}