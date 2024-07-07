package banking.management;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class config {

    @Bean
    public OpenAPI customOpenAPI(){
        Server server = new Server();
        String devURL = "http://localhost:9023";
        server.setUrl(devURL);
        server.setDescription("Server URL");

        //http://localhost:8080/swagger-ui/index.html#/

        Contact contact = new Contact();
        contact.setUrl("https://www.google.com");
        contact.setName("Jinesh Lalwani");
        contact.setEmail("john.doe@gmail.com");

        License license = new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info().title("Banking Management System").version("1.0").contact(contact).description("h").license(license).termsOfService("http://www.google.com");

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
