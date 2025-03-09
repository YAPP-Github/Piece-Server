package org.yapp.domain.setting.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.setting.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {

  Optional<Setting> findByUserId(Long userId);
}
