package server.spring.guide.event.domain.enums;


import lombok.Getter;

@Getter
public enum Step {
    FIRST(1, "1 단계"),
    SECOND(2, "2 단계"),
    THIRD(3, "3 단계");


    private final Integer value;
    private final String comment;

    Step(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }
}
