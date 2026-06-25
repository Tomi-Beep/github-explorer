package mk.ukim.finki.wp.githubexplorer.model.enums;

public enum RepositoryCategory {
    TO_STUDY("To study"),
    PORTFOLIO_INSPIRATION("Portfolio inspiration"),
    BACKEND("Backend"),
    DESKTOP("Desktop"),
    OTHER("Other");

    private final String displayName;

    RepositoryCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}