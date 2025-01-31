package org.yapp.domain.match.presentation.dto.response;

import java.time.LocalDate;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.profile.Profile;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class MatchProfileBasicResponse {

  private Long matchId;
  private String description;
  private String nickname;
  private Integer age;
  private String birthYear;
  private Integer height;
  private Integer weight;
  private String location;
  private String job;
  private String smokingStatus;

  public static MatchProfileBasicResponse fromProfile(Long matchId, Profile profile) {
    String nickname = profile.getProfileBasic().getNickname();
    LocalDate birthDate = profile.getProfileBasic().getBirthdate();
    LocalDate now = LocalDate.now();
    Integer age = Period.between(birthDate, now).getYears();
    String birthYearFormatted = String.valueOf(birthDate.getYear()).substring(2);
    String location = profile.getProfileBasic().getLocation();
    String description = profile.getProfileBasic().getDescription();
    Integer height = profile.getProfileBasic().getHeight();
    Integer weight = profile.getProfileBasic().getWeight();
    String job = profile.getProfileBasic().getJob();
    String smokingStatus = profile.getProfileBasic().getSmokingStatus();
    return new MatchProfileBasicResponse(matchId, description, nickname, age, birthYearFormatted
        , height, weight, location, job, smokingStatus);
  }
}
