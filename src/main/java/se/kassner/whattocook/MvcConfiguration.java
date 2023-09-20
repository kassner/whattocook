package se.kassner.whattocook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.concurrent.TimeUnit;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/dist/**")
            .addResourceLocations("classpath:/static/dist/")
            .setCacheControl(CacheControl.maxAge(4, TimeUnit.HOURS))
            .resourceChain(true)
            .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
        ;
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter()
    {
        return new ResourceUrlEncodingFilter();
    }
}
