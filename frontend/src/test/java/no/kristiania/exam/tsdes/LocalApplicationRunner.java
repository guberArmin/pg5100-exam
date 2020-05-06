package no.kristiania.exam.tsdes;

import org.springframework.boot.SpringApplication;

/**
 * This class is copy of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/frontend/src/test/java/org/tsdes/intro/exercises/quizgame/LocalApplicationRunner.java
 */
public class LocalApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, "--spring.profiles.active=test");
    }

}
