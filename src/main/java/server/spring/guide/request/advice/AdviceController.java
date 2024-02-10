package server.spring.guide.request.advice;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.dto.UserDTO;

public class AdviceController {
    @PostMapping("/users")
    public ResponseEntity<UserDTO> users(@RequestBody @Valid UserDTO user) {
        return ResponseEntity.ok(user);
    }
}
