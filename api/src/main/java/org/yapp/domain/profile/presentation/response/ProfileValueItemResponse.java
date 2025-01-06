package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.ProfileValuePick;

import lombok.Getter;

@Getter
public class ProfileValueItemResponse {
  private Long id;
  private Long valueItemId;
  private String question;
  private Integer selectedAnswer;

  public ProfileValueItemResponse(ProfileValuePick profileValuePick) {
    this.id = profileValuePick.getId();
    this.valueItemId = profileValuePick.getValuePick().getId();
    this.question = profileValuePick.getValuePick().getQuestion();
    this.selectedAnswer = profileValuePick.getSelectedAnswer();
  }
}