package org.yapp.domain.auth.application.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.ProfileBio;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.profile.application.dto.ProfileCreateDto;
import org.yapp.domain.profile.application.util.NickNameGenerator;
import org.yapp.domain.profile.dao.ProfileRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ProfileService 테스트")
class ProfileServiceTest {

  @Mock
  private ProfileRepository profileRepository;

  @InjectMocks
  private ProfileService profileService;

  @Test
  @DisplayName("프로필 생성 테스트 - 성공적으로 프로필을 생성한다.")
  void shouldCreateProfileSuccessfully() {
    final String nickName = NickNameGenerator.generateNickname();

    // Given: 프로필 생성에 필요한 DTO와 Mock 설정
    ProfileCreateDto dto = new ProfileCreateDto("홍길동", nickName, "010-1234-5678");
    Profile profile = Profile.builder()
                             .profileBasic(
                                 ProfileBasic.builder().nickname(nickName).phoneNumber(dto.phoneNumber()).build())
                             .profileBio(ProfileBio.builder().build())
                             .build();

    MockitoAnnotations.openMocks(this);
    when(profileRepository.save(any(Profile.class))).thenReturn(profile);

    // When: 프로필 생성 메서드를 호출
    Profile savedProfile = profileService.create(dto);

    // Then: 저장 동작이 한 번 실행되고, 결과가 예상대로 반환됨
    verify(profileRepository, times(1)).save(any(Profile.class));

    // Then: 저장된 프로필 객체가 전달받은 값과 일치하는지 검증
    ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
    verify(profileRepository).save(profileCaptor.capture());
    Profile capturedProfile = profileCaptor.getValue();

    assertThat(capturedProfile.getProfileBasic().getNickname()).isEqualTo("010-1234-5678");
    assertThat(capturedProfile.getProfileBasic().getPhoneNumber()).isEqualTo("010-1234-5678");
    assertThat(savedProfile).isNotNull();
  }
}