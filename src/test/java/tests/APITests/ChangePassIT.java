package tests.APITests;

import com.github.javafaker.Faker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import sample.Application;
import tests.Order;
import tests.OrderedRunner;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 24.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@TestPropertySource(locations = "classpath:test.properties")
public class ChangePassIT {

    @Autowired
    private MockMvc mockMvc;
    private static Faker faker;

    private static String userMail;
    private static String userLogin;
    private static String pass;

    @BeforeClass
    public static void setUp() {
        faker = new Faker(new Locale("en-US"));
        userMail = faker.internet().emailAddress();
        userLogin = faker.name().username();
        pass = faker.internet().password();
    }

    @Test
    @Order(order = 1)
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

    @Test
    @Order(order = 2)
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
    @Order(order = 3)
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
    @Order(order = 4)
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
    @Order(order = 5)
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
    @Order(order = 6)
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
    @Order(order = 7)
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
    @Order(order = 8)
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
    @Order(order = 9)
    public void invalidContent() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("text/html"))
                .andExpect(status().isUnsupportedMediaType());
    }
}