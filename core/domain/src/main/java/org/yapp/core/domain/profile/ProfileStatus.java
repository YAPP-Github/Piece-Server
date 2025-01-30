package org.yapp.core.domain.profile;

public enum ProfileStatus {

    INCOMPLETE("미완료"),
    REJECTED("보류"),
    REVISED("수정 제출"),
    APPROVED("통과");

    private final String displayName;


    ProfileStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProfileStatus fromDisplayName(String displayName) {
        for (ProfileStatus status : ProfileStatus.values()) {
            if (status.displayName.equals(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid display name: " + displayName);
    }
}
