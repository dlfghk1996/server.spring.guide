package server.spring.guide.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import server.spring.guide.event.domain.enums.Point;
import server.spring.guide.event.domain.enums.Progress;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Builder
@ToString
public class UserPay implements Serializable {

  private static final int DEFAULT_POINT = 0;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "total_stamp")
  private int totalStamp;

  @Column(name = "stamp")
  private int stamp;

  @Column(name = "point")
  private int point;

  public void plusPoint(Progress progress) {
    this.point += Point.calculate(progress);
  }

  public static UserPay ofZeroPoint(Long userId) {
    return new UserPay(userId, DEFAULT_POINT);
  }

  public UserPay (Long userId, Integer point) {
    this.userId = userId;
    this.totalStamp = point;
    this.stamp = point;
    this.point = point;
  }
}