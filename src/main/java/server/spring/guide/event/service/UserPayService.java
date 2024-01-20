package server.spring.guide.event.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.spring.guide.event.domain.UserPay;
import server.spring.guide.event.domain.enums.Point;
import server.spring.guide.event.domain.enums.Progress;
import server.spring.guide.event.repository.UserPayRepository;



@RequiredArgsConstructor
@Service
public class UserPayService {
    private final UserPayRepository userPayRepository;

    @Transactional
    public UserPay create(Long userId) {
        return userPayRepository.save(UserPay.ofZeroPoint(userId));
    }

    @Transactional
    public UserPay add(Long userId, Progress progress) {

        UserPay userPay = userPayRepository.findByUserId(userId).get();
        userPay.setPoint(Point.valueOf(String.valueOf(progress)).getValue()+userPay.getPoint());

        if(progress.equals(Progress.SUCCESS)){
            userPay.setStamp(userPay.getStamp()+1);
            userPay.setTotalStamp(userPay.getTotalStamp()+1);
        }
        return userPay;
    }
}
