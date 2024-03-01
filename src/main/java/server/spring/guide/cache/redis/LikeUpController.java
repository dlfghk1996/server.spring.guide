package server.spring.guide.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.cache.redis.service.LikeUpCacheService;
import server.spring.guide.common.dto.LikeUpDTO;
import server.spring.guide.common.dto.UserDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cache")
public class LikeUpController {

    private final LikeUpCacheService service;

    @PostMapping("")
    public LikeUpDTO add(@RequestBody UserDTO request){
        return service.addToRedis(request.getId());
    }

    @GetMapping("")
    public List<Object> list(){
        return service.getList();
    }


    @GetMapping("{userId}")
    public LikeUpDTO get(@PathVariable Long userId){
        return service.getUserLikeUpCntToRedis(userId);
    }

}
