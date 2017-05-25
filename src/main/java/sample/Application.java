package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"sample.Main.Controllers",
        "sample.Main.Services",
        "sample.Main.DAO",
        "sample.Main.Models",
        "sample.Main.Views",
        "sample.WebSockets",
        "sample.Lobby.Services",
        "sample.Game.Services",
        "sample.ResourceManager"
})
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}