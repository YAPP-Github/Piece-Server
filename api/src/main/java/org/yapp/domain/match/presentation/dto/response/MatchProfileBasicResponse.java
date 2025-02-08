package org.yapp.domain.match.presentation.dto.response;

import java.time.LocalDate;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;

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
    ProfileBasic profileBasic = profile.getProfileBasic();
    String nickname = profileBasic.getNickname();
    LocalDate birthDate = profileBasic.getBirthdate();
    LocalDate now = LocalDate.now();
    Integer age = Period.between(birthDate, now).getYears();
    String birthYearFormatted = String.valueOf(birthDate.getYear()).substring(2);
    String location = profileBasic.getLocation();
    String description = profileBasic.getDescription();
    Integer height = profileBasic.getHeight();
    Integer weight = profileBasic.getWeight();
    String job = profileBasic.getJob();
    String smokingStatus = profileBasic.getSmokingStatus();
    return new MatchProfileBasicResponse(matchId, description, nickname, age, birthYearFormatted
        , height, weight, location, job, smokingStatus);
  }
}
