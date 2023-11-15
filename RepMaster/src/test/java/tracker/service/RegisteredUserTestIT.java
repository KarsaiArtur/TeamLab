package tracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import tracker.model.RegisteredUser;

@SpringBootTest
@AutoConfigureTestDatabase
public class RegisteredUserTestIT {
    @Autowired
    private RegisteredUserService registeredUserService;

    @Test
    public void canAddRegisteredUserToDB() throws Exception{
        RegisteredUser rUser = new RegisteredUser();
        registeredUserService.addRegisteredUser(rUser);
    }

    @Test
    public void findRegisteredUserInDB() throws Exception{
        RegisteredUser rUser = RegisteredUser.builder().userName("TestUser").password("abc123").build();
        registeredUserService.addRegisteredUser(rUser);

        RegisteredUser copyUser = registeredUserService.findUserByName(rUser.getUserName());

        assertThat(copyUser.getUserName()).isEqualTo(rUser.getUserName());
    }

}
