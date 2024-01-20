package server.spring.guide.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.event.dto.AttendanceCycleDTO;
import server.spring.guide.event.service.AttendanceCycleService;

// 챌린지에 도전하고 매일 챌린지에 해당하는 활동을 인증하여 총 3회 인증 시 성공하는 사이클 (3회인증1사이클)
// 인증로직에 -> 랭킹점수 올리는 기능 추가
// 이벤트 적용, 트랜잭션 분리, 비동기 처리
// 랭킹 정책: 3번의 인증에 따라 점수를 부여한다. (FIRST:10점, SECOND:30점, SUCCESS:60점)
@RestController
@RequestMapping("/attendancecheckcycle")
@RequiredArgsConstructor
public class AttendanceCheckCycleController {

    private final AttendanceCycleService attendanceCycleService;

    @PostMapping("")
    public ResponseEntity<AttendanceCycleDTO> add(@RequestBody AttendanceCycleDTO request)
        throws Exception {
        AttendanceCycleDTO response = attendanceCycleService.add(request);
        return ResponseEntity.ok(response);
    }

}

