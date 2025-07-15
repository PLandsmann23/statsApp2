package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.user.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
}
