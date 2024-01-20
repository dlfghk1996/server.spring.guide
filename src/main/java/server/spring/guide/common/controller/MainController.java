package server.spring.guide.common.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class MainController {


//    // (1) Argument Resolver TEST
//    @PostMapping
//    public ResponseEntity<InitialDTO> argumentResolverTEST(@InitialAnnotaion @RequestBody InitialDTO request) {
//        System.out.println("message: " + request.getMessage());
//
//        log.debug("==================== MainController ====================");
//        return ResponseEntity.ok().build();
//    }

}
