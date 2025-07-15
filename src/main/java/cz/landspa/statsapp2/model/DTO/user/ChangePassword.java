package cz.landspa.statsapp2.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangePassword {

    String oldPassword;

    String newPassword;
}
