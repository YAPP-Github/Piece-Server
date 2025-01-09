//package org.yapp.domain.auth.application.profile;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import org.yapp.domain.auth.application.dummy.TestDataService;
//import org.yapp.domain.profile.Profile;
//import org.yapp.domain.profile.ProfileValuePick;
//import org.yapp.domain.profile.application.ProfileService;
//import org.yapp.domain.profile.application.dto.ProfileCreateDto;
//import org.yapp.domain.profile.dao.ProfileRepository;
//import org.yapp.domain.profile.dao.ProfileValuePickRepository;
//import org.yapp.domain.profile.presentation.request.ProfileUpdateRequest;
//import org.yapp.domain.profile.presentation.request.ProfileValuePickPair;
//import org.yapp.domain.profile.presentation.request.ProfileValuePickUpdateRequest;
//import org.yapp.domain.user.User;
//import org.yapp.domain.user.dao.UserRepository;
//
//@DisplayName("ProfileService 통합 테스트")
//@SpringBootTest
//@Transactional
//class ProfileServiceTest {
//
//    @Autowired
//    private ProfileService profileService;
//
//    @Autowired
//    private ProfileRepository profileRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TestDataService testDataService;
//
//    @Autowired
//    private ProfileValuePickRepository profileValueRepository;
//
//    private User testUser;
//    private Profile testProfile;
//
//    @BeforeEach
//    void setUp() {
//        profileRepository.deleteAll();
//
//        testUser = User.builder().name("test@naver.com").build();
//
//        ProfileCreateDto dto = ProfileCreateDto.builder().nickName("nickname123")
//            .phoneNumber("010-5133-2895").build();
//        testProfile = profileService.create(dto);
//
//        testUser.setProfile(testProfile);
//        userRepository.save(testUser);
//        testDataService.createValueItems();
//    }
//
//    @Test
//    @DisplayName("프로필 생성 테스트 - 실제 DB에서 성공적으로 프로필을 생성한다.")
//    void shouldCreateProfileSuccessfully() {
//        // Given
//        ProfileCreateDto dto = new ProfileCreateDto("홍길동", "nickname123", "010-1234-5678");
//
//        // When
//        Profile savedProfile = profileService.create(dto);
//
//        // Then
//        assertThat(savedProfile).isNotNull();
//        assertThat(savedProfile.getProfileBasic().getNickname()).isEqualTo("nickname123");
//        assertThat(savedProfile.getProfileBasic().getPhoneNumber()).isEqualTo("010-1234-5678");
//        assertThat(savedProfile.getProfileValuePicks().size()).isEqualTo(
//            profileValueRepository.count());
//    }
//
//    @Test
//    @DisplayName("유저 ID로 프로필 업데이트 테스트")
//    void shouldUpdateProfileByUserId() {
//        // Given: 업데이트 요청 생성
//        ProfileUpdateRequest updateRequest =
//            new ProfileUpdateRequest("updatedNickname", "1995-05-20", 172, "개발자", "서울시 강남구", "비흡연",
//                "기독교", "활발",
//                "01011112222", "https://example.com/profile.jpg", "안녕하세요. 개발자입니다.",
//                "10년 안에 CTO가 되는 것", "기술, 클라이밍, 여행");
//
//        // When: 업데이트 실행
//        Profile updatedProfile = profileService.updateByUserId(testUser.getId(), updateRequest);
//
//        // Then: 업데이트 결과 검증 (모든 필드)
//        assertThat(updatedProfile).isNotNull();
//
//        assertThat(updatedProfile.getProfileBasic().getNickname()).isEqualTo(
//            updateRequest.nickname());
//        assertThat(updatedProfile.getProfileBasic().getPhoneNumber()).isEqualTo(
//            updateRequest.phoneNumber());
//        assertThat(updatedProfile.getProfileBasic().getJob()).isEqualTo(updateRequest.job());
//        assertThat(updatedProfile.getProfileBasic().getHeight()).isEqualTo(updateRequest.height());
//        assertThat(updatedProfile.getProfileBasic().getBirthdate().toString()).isEqualTo(
//            updateRequest.birthdate());
//        assertThat(updatedProfile.getProfileBasic().getLocation()).isEqualTo(
//            updateRequest.location());
//        assertThat(updatedProfile.getProfileBasic().getSmokingStatus()).isEqualTo(
//            updateRequest.smokingStatus());
//        assertThat(updatedProfile.getProfileBasic().getReligion()).isEqualTo(
//            updateRequest.religion());
//        assertThat(updatedProfile.getProfileBasic().getSnsActivityLevel()).isEqualTo(
//            updateRequest.snsActivityLevel());
//        assertThat(updatedProfile.getProfileBasic().getImageUrl()).isEqualTo(
//            updateRequest.imageUrl());
//
//        assertThat(updatedProfile.getProfileBio().getIntroduction()).isEqualTo(
//            updateRequest.introduction());
//        assertThat(updatedProfile.getProfileBio().getGoal()).isEqualTo(updateRequest.goal());
//        assertThat(updatedProfile.getProfileBio().getInterest()).isEqualTo(
//            updateRequest.interest());
//    }
//
//    @Test
//    @DisplayName("프로필 값 업데이트 테스트 - 정상 동작")
//    void updateProfileValuePicks_Success() {
//        // given
//        Long userId = testUser.getId();
//        ProfileCreateDto dto = new ProfileCreateDto("홍길동", "nickname123", "010-1234-5678");
//        Profile savedProfile = profileService.create(dto);
//        testUser.setProfile(savedProfile);
//        profileRepository.flush();
//
//        // 업데이트 요청 생성 (ValueItem 2개 선택)
//        List<ProfileValuePickPair> profileValuePickPairList = new ArrayList<>();
//        savedProfile.getProfileValuePicks().forEach((e) -> {
//            profileValuePickPairList.add(new ProfileValuePickPair(e.getValuePick().getId(), 1));
//        });
//
//        ProfileValuePickUpdateRequest updateRequest = new ProfileValuePickUpdateRequest(
//            profileValuePickPairList
//        );
//
//        // when
//        profileService.updateProfileValues(userId, updateRequest);
//
//        // then
//        List<ProfileValuePick> updatedProfileValuePicks = profileValueRepository.findByProfileId(
//            savedProfile.getId());
//
//        // 업데이트 검증
//        updatedProfileValuePicks.forEach(profileValue -> {
//            Long valueItemId = profileValue.getValuePick().getId();
//            Integer expectedAnswer = updateRequest.profileValuePickPairs().stream()
//                .filter(pair -> pair.valueItemId().equals(valueItemId))
//                .findFirst()
//                .map(ProfileValuePickPair::selectedAnswer)
//                .orElse(null);
//
//            assertThat(profileValue.getSelectedAnswer()).isEqualTo(expectedAnswer);
//        });
//    }
//}