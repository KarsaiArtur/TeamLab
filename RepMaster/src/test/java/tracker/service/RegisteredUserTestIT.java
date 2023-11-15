package tracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.Rating;
import tracker.model.RegisteredUser;

@SpringBootTest
@AutoConfigureTestDatabase
public class RegisteredUserTestIT {
    @Autowired
    private RegisteredUserService registeredUserService;

    @Test
    public void canAddRegisteredUserToDB() throws Exception{
        RegisteredUser rUser = new RegisteredUser();
        registeredUserService.addRegistereduser(rUser);
    }

}
