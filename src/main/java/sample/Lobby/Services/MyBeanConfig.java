package sample.Lobby.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ksg on 15.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
@Configuration
public class MyBeanConfig {
    @Bean
    public ObjectMapper ObjectMapper() {
        return new ObjectMapper();
    }
}
