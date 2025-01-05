package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.ProfileValueItem;

import lombok.Getter;

@Getter
public class ProfileValueItemResponse {
  private Long id;
  private Long valueItemId;
  private String question;
  private Integer selectedAnswer;

  public ProfileValueItemResponse(ProfileValueItem profileValueItem) {
    this.id = profileValueItem.getId();
    this.valueItemId = profileValueItem.getValueItem().getId();
    this.question = profileValueItem.getValueItem().getQuestion();
    this.selectedAnswer = profileValueItem.getSelectedAnswer();
  }
}