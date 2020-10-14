package pl.pfranczak.j2bills2.monolith.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mapping.properties")
public class PropertyFilesConfiguration {

}
