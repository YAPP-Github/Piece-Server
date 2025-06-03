package org.yapp.domain.match.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.application.ProfileValuePickService;
import org.yapp.domain.user.application.UserService;

@Component
@RequiredArgsConstructor
public class InstantMatchVectorGenerator {

    private final ProfileValuePickService profileValuePickService;
    private final UserService userService;

    private List<Double> oneHotEncode(int value) {
        List<Double> oneHot = Arrays.asList(0D, 0D, 0D);
        oneHot.set(value - 1, 1D);
        return oneHot;
    }

    public List<Double> generate(Profile profile) {
        List<ProfileValuePick> valuePicks = profileValuePickService.getAllProfileValuePicksByProfileId(
            profile.getId());

        List<Double> vector = new ArrayList<>();
        for (ProfileValuePick valuePick : valuePicks) {
            Integer selectedAnswer = valuePick.getSelectedAnswer();
            List<Double> encodedValue = oneHotEncode(selectedAnswer);
            vector.addAll(encodedValue);
        }

        return vector;
    }
}
