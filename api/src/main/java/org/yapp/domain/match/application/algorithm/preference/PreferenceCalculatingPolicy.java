package org.yapp.domain.match.application.algorithm.preference;

public interface PreferenceCalculatingPolicy {

  int calculatePreference(Long profile1Id, Long profile2Id);
}
