package dev.hub.security.config;

import dev.hub.security.system.AppIntercepter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfing implements WebMvcConfigurer {

    private final AppIntercepter appIntercepter;

    /**
     * InterCeptor start
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(appIntercepter)
                .addPathPatterns("/index.jsp")
                .addPathPatterns("/**/*.do")
                .excludePathPatterns("/").excludePathPatterns();
    }
}
