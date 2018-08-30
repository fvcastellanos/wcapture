package net.cavitos.wcapture.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:git.properties"})
public class PropertySourcesPlaceholderConfiguration {

}
