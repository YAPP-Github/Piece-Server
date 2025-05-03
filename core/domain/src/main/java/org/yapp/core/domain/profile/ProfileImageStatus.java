package org.yapp.core.domain.profile;

public enum ProfileImageStatus {
    PENDING("심사중"),
    ACCEPTED("통과"),
    REJECTED("거절");

    private final String displayName;

    ProfileImageStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
s