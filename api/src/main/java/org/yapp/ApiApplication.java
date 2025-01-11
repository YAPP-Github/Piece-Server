package org.yapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
public class ApiApplication {
  private final TestBean testBean;

  @Autowired
  public ApiApplication(TestBean testBean) {
    this.testBean = testBean;
  }

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @PostConstruct
  public void dependencyTest() {
    testBean.dependencyTest();
  }
}