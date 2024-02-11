package server.spring.guide.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserLikeUp {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Long userId;
        private int count;

        // 최신 Version 추적 (동시성이슈 : OPTIMISTIC Lock)
        @Version
        private Long version;

        // 좋아요 개수 차감 메서드
        public int decrease(int count) {
                if (this.count < count) {
                        throw new IllegalArgumentException("Not enough count");
                }

                this.count -= count;
                return this.count;
        }
}
