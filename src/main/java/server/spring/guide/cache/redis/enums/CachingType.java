package server.spring.guide.cache.redis.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CachingType {
    TOPRANKING("TOP_RANKING", "조회수"),
    LIKE("LIKE", "좋아요."),
    RANKING("WEEK_RANKING", "좋아요 랭킹");

    @JsonValue
    private final String code;
    private final String label;

    CachingType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name();
    }

    public String getLabel() {
        return this.label;
    }
}

