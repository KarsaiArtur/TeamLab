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
        rUser = RegisteredUser.builder().userName("TestUser").password("abc123").build();
        registeredUserService.addRegisteredUser(rUser);
    }

    @Test
    public void findRegisteredUserInDB() throws Exception{
        RegisteredUser copyUser = registeredUserService.findUserByName(rUser.getUserName());

        assertThat(copyUser.getUserName()).isEqualTo(rUser.getUserName());
    }

    @Test
    public void loginAnExistingUser() throws Exception{
        String loginMessage = registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());

        assertThat(loginMessage).isEqualTo("Login successful as "+rUser.getUserName());
    }

    @Test
    public void loginANonExistingUser() throws Exception{
        String loginMessage = registeredUserService.loginUser("TestUser2", "");

        assertThat(loginMessage).isEqualTo("Login failed: no User with such username");
    }

    @Test
    public void loginWithWrongPassword() throws Exception{
        String loginMessage = registeredUserService.loginUser(rUser.getUserName(), "");

        assertThat(loginMessage).isEqualTo("Login failed: wrong password");
    }

    @Test
    public void createUserWithExistingName() throws Exception{
        String accountCreateMessage = registeredUserService.addRegisteredUser(RegisteredUser.builder().userName("TestUser").password("abc123").build());

        assertThat(accountCreateMessage).isEqualTo("Account creation failed: username already exists");
    }



}
