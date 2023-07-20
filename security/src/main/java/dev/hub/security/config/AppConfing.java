package dev.hub.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfing {

    private final AppIntercepter appIntercepter;

    /**
     * InterCeptor start
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(appIntercepter).excludePathPatterns("/").excludePathPatterns();
    }
}
