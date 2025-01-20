package org.yapp.domain.match.application.algorithm;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.blocker.Blocker;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.application.ProfileValuePickService;

@Component
@RequiredArgsConstructor
public class PreferenceBasedMatchingAlgorithm {

    private final List<Blocker> blockers;
    private final ProfileValuePickService profileValuePickService;

    /**
     * 가치관 기반으로 매칭을 수행
     *
     * @param profiles 매칭 수행할 프로필 리스트
     * @return 매칭이 이루어지지 않은 프로필 리스트
     */
    public List<Profile> doMatch(List<Profile> profiles) {
        //TODO : 효율적인 일반 그래프 매칭 알고리즘 구현
        //TODO : 요구조건 - 차단한 사용자 x, 이전에 매칭된 사용자 x, 지인 x

        return List.of();
    }

    private int calculateWeight(Long fromProfileId, Long toProfileId) {
        List<ProfileValuePick> profileValuePicksOfFrom =
            profileValuePickService.getAllProfileValuePicksByProfileId(fromProfileId);
        List<ProfileValuePick> profileValuePicksOfTo = profileValuePickService.getAllProfileValuePicksByProfileId(
            toProfileId);

        int valueListSize = profileValuePicksOfFrom.size();
        int sumOfWeight = 0;
        for (int i = 0; i < valueListSize; i++) {
            ProfileValuePick profileValuePickOfFrom = profileValuePicksOfFrom.get(i);
            ProfileValuePick profileValuePickOfTo = profileValuePicksOfTo.get(i);
            if (profileValuePickOfFrom.getSelectedAnswer()
                .equals(profileValuePickOfTo.getSelectedAnswer())) {
                sumOfWeight++;
            }
        }
        return sumOfWeight;
    }

    private boolean checkBlock(Long user1, Long user2) {
        for (Blocker blocker : blockers) {
            boolean blocked = blocker.blocked(user1, user2);
            if (blocked) {
                return true;
            }
        }
        return false;
    }
}
