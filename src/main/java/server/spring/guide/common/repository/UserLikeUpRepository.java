package server.spring.guide.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.domain.UserLikeUp;

public interface UserLikeUpRepository extends JpaRepository<UserLikeUp, Long> {

   // void addViewCntFromRedis(Long productId, Long viewCnt);

   // List<UserLikeUp> findAllByCreatedAtAfterOrderByCreatedAtDesc(String cursor);

    void findByUserId(Long userId);

//            public void addViewCntFromRedis(Long productId,Long addCnt) {
//                queryFactory
//                    .update(product)
//                    .set(product.productViewCnt,addCnt)
//                    .where(product.productId.eq(productId))
//                    .execute();
//           }
}
