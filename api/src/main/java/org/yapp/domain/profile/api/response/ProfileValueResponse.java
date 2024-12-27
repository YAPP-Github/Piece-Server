package org.yapp.domain.profile.api.response;

import org.yapp.domain.profile.ProfileValue;

import lombok.Getter;

@Getter
public class ProfileValueResponse {
  private Long id;
  private Long valueItemId;
  private String question;
  private Integer selectedAnswer;

  public ProfileValueResponse(ProfileValue profileValue) {
    this.id = profileValue.getId();
    this.valueItemId = profileValue.getValueItem().getId();
    this.question = profileValue.getValueItem().getQuestion();
    this.selectedAnswer = profileValue.getSelectedAnswer();
  }
}