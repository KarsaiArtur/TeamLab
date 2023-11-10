package tracker;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tracker.service.InitDbService;

@RequiredArgsConstructor
@SpringBootApplication
public class TrackerApplication implements CommandLineRunner{
    private final InitDbService initDbService;

    @Override
    public void run(String... args) throws Exception{
    }
    public static void main(String[] args){
        SpringApplication.run(TrackerApplication.class, args);
    }
}
