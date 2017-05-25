package tests.UnitTests;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import sample.Application;
import sample.Main.Models.UserData;
import sample.Main.Services.AccountService;
import tests.OrderedRunner;

import java.util.Locale;

import static org.junit.Assert.assertSame;

/**
 * Created by lieroz on 27.03.17.
 */

@SuppressWarnings("DefaultFileTemplate")
@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class TestRegistration {
    @Autowired
    private AccountService accountService;

    private static Faker faker;
    private static String userMail;
    private static String userLogin;
    private static String pass;

    @BeforeClass
    public static void setUpFaker() {
        faker = new Faker(new Locale("en-US"));
    }

    public void registerUserOk() {
        final UserData userData = new UserData(userMail, userLogin, pass);
        final AccountService.ErrorCodes error = accountService.register(userData);
        assertSame(error, AccountService.ErrorCodes.OK);
    }

    @Before
    public void setUp() {
        userMail = faker.internet().emailAddress();
        userLogin = faker.name().username();
        pass = faker.internet().password();
        registerUserOk();
    }

    @Test
    public void registerUserConflict() {
        final UserData userData = new UserData(userMail, userLogin, pass);
        final AccountService.ErrorCodes error = accountService.register(userData);
        assertSame(error, AccountService.ErrorCodes.LOGIN_OCCUPIED);
    }

    @Test
    public void registerUserNullMail() {
        final UserData userData = new UserData(
                null,
                faker.name().username(),
                faker.internet().password());
        final AccountService.ErrorCodes error = accountService.register(userData);
        assertSame(error, AccountService.ErrorCodes.INVALID_REG_DATA);
    }

    @Test
    public void registerNullLogin() {
        final UserData userData = new UserData(
                faker.internet().emailAddress(),
                null,
                faker.internet().password());
        final AccountService.ErrorCodes error = accountService.register(userData);
        assertSame(error, AccountService.ErrorCodes.INVALID_REG_DATA);
    }

    @Test
    public void registerUserNullPass() {
        final UserData userData = new UserData(
                faker.internet().emailAddress(),
                faker.name().username(),
                null);
        final AccountService.ErrorCodes error = accountService.register(userData);
        assertSame(error, AccountService.ErrorCodes.INVALID_REG_DATA);
    }
}
