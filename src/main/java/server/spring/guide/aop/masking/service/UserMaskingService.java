package server.spring.guide.aop.masking.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import server.spring.guide.aop.masking.annotation.ApplyMasking;
import server.spring.guide.common.dto.UserDTO;

public class UserMaskingService {
    private HashMap<String, UserDTO> userMap;

    @PostConstruct
    public void init(){
        userMap = new HashMap<>();
        userMap.put("1",new UserDTO(1L,"홍길동","010-1234-5678","honggildong@email.com"));
        userMap.put("2",new UserDTO(2L,"이영희","010-9999-8765","yhlee@email.com"));
        userMap.put("3",new UserDTO(3L,"김철수","010-5678-4321","ironwater@email.com"));
    }

    @ApplyMasking(typeValue = UserDTO.class)
    public UserDTO getUserInfo(UserDTO request){
        return userMap.get(request.getId());
    }

    @ApplyMasking(typeValue = List.class, genericTypeValue = UserDTO.class)
    public List<UserDTO> getUserInfoList(){
        return new ArrayList<>(userMap.values());
    }
}
