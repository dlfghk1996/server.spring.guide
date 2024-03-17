package server.spring.guide.running;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class CustomApplicationRunner implements ApplicationRunner {
    private static final Logger LOG =
        LoggerFactory.getLogger(CustomApplicationRunner.class);
    @Autowired
    InitBean initBean;

    // ApplicationArguments : prefix '--' 로 시작하는 argument info
    @Override
    public void run(ApplicationArguments args) throws Exception {

        LOG.info("Application started with option names : {}", args.getOptionNames());
        LOG.info(
            Arrays.asList(initBean.getEnvironment().getDefaultProfiles()).toString());
    }
}
