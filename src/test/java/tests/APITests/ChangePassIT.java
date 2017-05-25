package tests.APITests;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import sample.Application;
import tests.IntegrationTest;
import tests.OrderedRunner;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by lieroz on 24.03.17.
 */

@SuppressWarnings("DefaultFileTemplate")
@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Category(IntegrationTest.class)
public class ChangePassIT {

    @Autowired
    private MockMvc mockMvc;
    private static Faker faker;

    private static String userMail;
    private static String userLogin;
    private static String pass;

    @BeforeClass
    public static void setUpFaker() {
        faker = new Faker(new Locale("en-US"));
    }

    public void createUser() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + userMail + "\"," +
                                "\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.errorMsg", is("User created successfully! en")));
    }

    @Before
    public void setUp() {
        userMail = faker.internet().emailAddress();
        userLogin = faker.name().username();
        pass = faker.internet().password();

        try {
            createUser();

        } catch (Exception ex) {
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new RuntimeException();
        }
    }

    @Test
    public void nullSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                                "\"newPass\":\"" + faker.internet().password() + "\"}"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Invalid session! en")));
    }

    @Test
    public void invalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                                "\"newPass\":\"" + faker.internet().password() + "\"}")
                        .sessionAttr("userLogin", faker.name().username()))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Invalid authentication data! en")));
    }

    @Test
    public void nullOldPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":" + null + "," +
                                "\"newPass\":\"" + faker.internet().password() + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Json contains null fields! en")));
    }

    @Test
    public void nullNewPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                                "\"newPass\":" + null + "}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Json contains null fields! en")));
    }

    @Test
    public void invalidOldPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + faker.internet().password() + "\"," +
                                "\"newPass\":\"" + faker.internet().password() + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Invalid authentication data! en")));
    }

    @Test
    public void changePassOk() throws Exception {
        final String newPass = faker.internet().password();
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                                "\"newPass\":\"" + newPass + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.errorMsg", is("Ok! en")));
        pass = newPass;
    }

    @Test
    public void loginOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/login")
                        .contentType("application/json")
                        .content("{\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.errorMsg", is("Ok! en")));
    }

    @Test
    public void invalidContent() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("text/html"))
                .andExpect(status().isUnsupportedMediaType());
    }
}