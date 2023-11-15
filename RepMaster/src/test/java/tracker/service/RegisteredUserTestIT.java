package tracker.service;

import org.junit.jupiter.api.BeforeEach;
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
    private RegisteredUser rUser;

    @BeforeEach
    public void clearAndInitDb() {
        registeredUserService.deleteAll();
        RegisteredUser rUser = RegisteredUser.builder().userName("TestUser").password("abc123").build();
        registeredUserService.addRegisteredUser(rUser);
    }
    @Test
    public void canAddRegisteredUserToDB() throws Exception{
    }

    @Test
    public void findRegisteredUserInDB() throws Exception{
        RegisteredUser copyUser = registeredUserService.findUserByName(rUser.getUserName());

        assertThat(copyUser.getUserName()).isEqualTo(rUser.getUserName());
    }

}
