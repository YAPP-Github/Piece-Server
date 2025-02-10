package org.yapp.domain.profile.presentation.response;

import java.time.LocalDate;
import java.time.Period;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;

public record ProfileBasicPreviewResponse(String description, String nickname, Integer age,
                                          String birthYear,
                                          Integer height, Integer weight, String location,
                                          String job,
                                          String imageUrl,
                                          String smokingStatus) {

    public static ProfileBasicPreviewResponse fromProfile(Profile profile) {
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
        String imageUrl = profileBasic.getImageUrl();
        String smokingStatus = profileBasic.getSmokingStatus();

        return new ProfileBasicPreviewResponse(
            description, nickname, age, birthYearFormatted
            , height, weight, location, job, imageUrl, smokingStatus);
    }
}