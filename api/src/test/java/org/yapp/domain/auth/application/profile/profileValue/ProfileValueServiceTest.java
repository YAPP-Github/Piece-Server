package org.yapp.domain.auth.application.profile.profileValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.auth.application.dummy.TestDataService;
import org.yapp.domain.profile.ProfileValue;
import org.yapp.domain.profile.application.ProfileValueService;
import org.yapp.domain.profile.dao.ProfileValueRepository;
import org.yapp.domain.user.User;
import org.yapp.domain.value.ValueItem;
import org.yapp.error.exception.ApplicationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProfileValueServiceTest {

    @Autowired
    private ProfileValueService profileValueService;

    @Autowired
    private ProfileValueRepository profileValueRepository;

    @Autowired
    private TestDataService testDataService;

    private User savedUser;
    private List<ValueItem> savedValueItems;

    @BeforeEach
    void setUp() {
        savedUser = testDataService.createUserWithProfile("John Doe", "oauth123123213123");
        savedValueItems = testDataService.createValueItems();
    }

    @Test
    @DisplayName("ProfileValue 생성 테스트 - 정상 동작")
    void createAllProfileValues_Success() {
        // given
        Long profileId = savedUser.getProfile().getId();

        // when
        List<ProfileValue> createdValues = profileValueService.createAllProfileValues(profileId);

        // then
        assertThat(createdValues).isNotEmpty();
        assertThat(createdValues).hasSize(savedValueItems.size());

        createdValues.forEach(profileValue -> {
            assertThat(profileValue.getProfile()).isEqualTo(savedUser.getProfile());
            assertThat(profileValue.getSelectedAnswer()).isNull();  // 초기 생성 시 null
        });

        // DB에서 데이터 확인
        List<ProfileValue> savedProfileValues = profileValueRepository.findAll();
        assertThat(savedProfileValues).hasSize(savedValueItems.size());
    }

    @Test
    @DisplayName("존재하지 않는 Profile ID로 요청 시 예외 발생")
    void createAllProfileValues_ProfileNotFound() {
        // given
        Long invalidProfileId = 999L;

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(ApplicationException.class, () -> {
            profileValueService.createAllProfileValues(invalidProfileId);
        });
    }
}
