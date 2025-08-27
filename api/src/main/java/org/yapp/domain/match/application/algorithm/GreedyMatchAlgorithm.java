package org.yapp.domain.match.application.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.domain.match.application.MatchService;
import org.yapp.domain.match.application.algorithm.block.MatchBlocker;
import org.yapp.domain.match.application.algorithm.preference.PreferenceCalculatingPolicy;

@Primary
@RequiredArgsConstructor
@Slf4j
@Component
public class GreedyMatchAlgorithm implements MatchAlgorithm {

  private final MatchService matchService;
  private final MatchBlocker matchBlocker;
  private final PreferenceCalculatingPolicy preferenceCalculatingPolicy;

  @Override
  @Transactional
  public List<Profile> doMatch(List<Profile> profiles) {
    PriorityQueue<Edge> priorityEdges = getPriorityEdges(profiles);
    Set<Long> matchedSet = greedyMatch(priorityEdges);
    List<Profile> unmatchedProfiles = new ArrayList<>();
    for (Profile profile : profiles) {
      if (!matchedSet.contains(profile.getUser().getId())) {
        unmatchedProfiles.add(profile);
      }
    }
    return unmatchedProfiles;
  }

  private Set<Long> greedyMatch(PriorityQueue<Edge> priorityEdges) {
    Set<Long> matchedSet = new HashSet<>();
    while (!priorityEdges.isEmpty()) {
      Edge edge = priorityEdges.poll();
      if (matchedSet.contains(edge.user1Id) || matchedSet.contains(edge.user2Id)) {
        continue;
      }
      matchedSet.add(edge.user1Id);
      matchedSet.add(edge.user2Id);

      matchService.createMatchInfo(edge.user1Id, edge.user2Id, false);
    }
    return matchedSet;
  }

  private PriorityQueue<Edge> getPriorityEdges(List<Profile> profiles) {
    PriorityQueue<Edge> priorityEdgeQueue = new PriorityQueue<Edge>();
    for (Profile profile1 : profiles) {
      for (Profile profile2 : profiles) {
        if (profile1.getId().equals(profile2.getId())) {
          continue;
        }
        if (checkBlock(profile1, profile2)) {
          continue;
        }
        int weight = calculateWeight(profile1.getId(), profile2.getId());
        priorityEdgeQueue.add(
            new Edge(weight, profile1.getUser().getId(), profile2.getUser().getId()));
      }
    }
    return priorityEdgeQueue;
  }

  private boolean checkBlock(Profile blockingProfile, Profile blockedProfile) {
    return matchBlocker.isBlocked(blockedProfile, blockingProfile) ||
        matchBlocker.isBlocked(blockingProfile, blockedProfile);
  }

  private int calculateWeight(Long fromProfileId, Long toProfileId) {
    return preferenceCalculatingPolicy.calculatePreference(fromProfileId, toProfileId);
  }

  @AllArgsConstructor
  private static class Edge implements Comparable<Edge> {

    private int weight;
    private Long user1Id;
    private Long user2Id;

    @Override
    public int compareTo(Edge o) {
      return o.weight - this.weight;
    }
  }
}
