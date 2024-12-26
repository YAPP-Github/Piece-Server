package org.yapp.domain.profile.application.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NickNameGenerator {
  private static final List<String> ANIMAL_NAMES =
      List.of("사자", "호랑이", "곰", "독수리", "늑대", "판다", "여우", "사슴", "토끼", "돌고래");

  public static String generateNickname() {
    String animal = ANIMAL_NAMES.get(ThreadLocalRandom.current().nextInt(ANIMAL_NAMES.size()));
    int number = ThreadLocalRandom.current().nextInt(10000) + 1;
    return animal + number;
  }
}
