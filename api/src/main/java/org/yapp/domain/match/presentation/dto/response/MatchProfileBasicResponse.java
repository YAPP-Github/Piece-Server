package org.yapp.domain.match.presentation.dto.response;

import java.time.LocalDate;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.domain.profile.Profile;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class MatchProfileBasicResponse {

  private Long matchId;
  private String description;
  private String nickname;
  private String age;
  private String birthYear;
  private String location;
  private String job;

  public static MatchProfileBasicResponse fromProfile(Long matchId, Profile profile) {
    String nickname = profile.getProfileBasic().getNickname();
    LocalDate birthDate = profile.getProfileBasic().getBirthdate();
    LocalDate now = LocalDate.now();
    String age = String.valueOf(Period.between(birthDate, now).getYears());
    String birthYearFormatted = String.valueOf(birthDate.getYear()).substring(2);
    String location = profile.getProfileBasic().getLocation();
    String job = profile.getProfileBasic().getJob();
    return new MatchProfileBasicResponse(matchId, "", nickname, birthYearFormatted, age, location,
        job);
  }
}
