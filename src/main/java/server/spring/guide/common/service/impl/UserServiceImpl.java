package server.spring.guide.common.service.impl;



import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.spring.guide.common.service.UserService;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
        System.out.println("UserServiceImpl 생성자 호출");
    }
}
