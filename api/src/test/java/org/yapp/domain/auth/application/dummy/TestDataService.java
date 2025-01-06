package org.yapp.domain.auth.application.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.ProfileBio;
import org.yapp.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.dao.ProfileValueRepository;
import org.yapp.domain.user.User;
import org.yapp.domain.user.dao.UserRepository;
import org.yapp.domain.value.ValuePick;
import org.yapp.domain.value.dao.ValuePickRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TestDataService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValuePickRepository valuePickRepository;

    @Autowired
    private ProfileValueRepository profileValueRepository;

    // Profile 생성
    public Profile createProfile() {
        ProfileBio profileBio = ProfileBio.builder()
                .introduction("Hello, I am John.")
                .goal("Achieve success.")
                .interest("Coding")
                .build();

        ProfileBasic profileBasic = ProfileBasic.builder()
                .nickname("JohnDoe123")
                .birthdate(new Date(1990, 1, 1))
                .height(180)
                .job("Engineer")
                .location("Seoul")
                .smokingStatus("Non-smoker")
                .religion("None")
                .snsActivityLevel("Medium")
                .phoneNumber("010-1234-5678")
                .imageUrl("https://example.com/johndoe.jpg")
                .build();

        Profile profile = Profile.builder()
                .profileBasic(profileBasic)
                .profileBio(profileBio)
                .build();

        return profileRepository.save(profile);
    }

    // User 생성 및 Profile 연결
    public User createUserWithProfile(String userName, String oauthId) {
        Profile profile = createProfile();

        User user = User.builder()
                .name(userName)
                .oauthId(oauthId)
                .profile(profile)
                .role("USER")
                .build();

        return userRepository.save(user);
    }

    // ValueItem 더미 데이터 생성
    public List<ValuePick> createValueItems() {
        ValuePick valuePick1 = ValuePick.builder()
                .category("음주")
                .question("연인과 함께 술을 마시는 것을 좋아하나요?")
                .answers(Map.of(1, "함께 술을 즐기고 싶어요", 2, "같이 술을 즐길 수 없어도 괜찮아요"))
                .build();

        ValuePick valuePick2 = ValuePick.builder()
                .category("만남 빈도")
                .question("주말에 얼마나 자주 데이트를 하고 싶나요?")
                .answers(Map.of(1, "주말에는 최대한 같이 있고 싶어요", 2, "하루 정도는 각자 보내고 싶어요"))
                .build();

        ValuePick valuePick3 = ValuePick.builder()
                .category("연락 빈도")
                .question("연인 사이에 얼마나 자주 연락하는게 좋나요?")
                .answers(Map.of(1, "바쁘더라도 자주 연락하고 싶어요", 2, "연락은 생각날 때만 종종 해도 괜찮아요"))
                .build();

        ValuePick valuePick4 = ValuePick.builder()
                .category("연락 방식")
                .question("연락할 때 어떤 방법을 더 좋아하나요?")
                .answers(Map.of(1, "전화보다는 문자나 카톡이 더 좋아요", 2, "문자나 카톡보다는 전화가 더 좋아요"))
                .build();

        return valuePickRepository.saveAll(List.of(valuePick1, valuePick2, valuePick3, valuePick4));
    }

    // ProfileValue 더미 데이터 생성
    public void createProfileValues(Profile profile, List<ValuePick> valuePicks) {
        ProfileValuePick profileValuePick1 = ProfileValuePick.builder()
                .profile(profile)
                .valuePick(valuePicks.get(0))  // 음주
                .selectedAnswer(2)  // "같이 술을 즐길 수 없어도 괜찮아요"
                .build();

        ProfileValuePick profileValuePick2 = ProfileValuePick.builder()
                .profile(profile)
                .valuePick(valuePicks.get(1))  // 만남 빈도
                .selectedAnswer(1)  // "주말에는 최대한 같이 있고 싶어요"
                .build();

        profileValueRepository.saveAll(List.of(profileValuePick1, profileValuePick2));
    }
}