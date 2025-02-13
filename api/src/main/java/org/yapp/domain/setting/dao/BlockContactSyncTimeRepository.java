package org.yapp.domain.setting.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.setting.BlockContactSyncTime;

public interface BlockContactSyncTimeRepository extends JpaRepository<BlockContactSyncTime, Long> {

}
