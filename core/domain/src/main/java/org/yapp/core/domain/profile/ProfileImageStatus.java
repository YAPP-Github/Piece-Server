package org.yapp.core.domain.profile;

public enum ProfileImageStatus {
    INCOMPLETE("미완료"),
    ACCEPTED("통과"),
    REJECTED("보류");

    private final String displayName;

    ProfileImageStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
