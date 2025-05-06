package org.yapp.core.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.user.BannedUserPhoneNumber;

public interface BannedUserPhoneNumberRepository extends
    JpaRepository<BannedUserPhoneNumber, String> {

}
