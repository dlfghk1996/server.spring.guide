package server.spring.guide.cache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import server.spring.guide.cache.service.CacheService;
import server.spring.guide.common.domain.User;

@RequiredArgsConstructor
@Slf4j
@RestController
public class EhCacheController {

    private final CacheService cacheService;

}
