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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 24.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Category(IntegrationTest.class)
public class ChangeMailIT {

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
            throw new RuntimeException();
        }
    }

    @Test
    public void changeMailOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + faker.internet().emailAddress() + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.errorMsg", is("Ok! en")));
    }

    @Test
    public void changeMailNull() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"userMail\":" + null + "}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Json contains null fields! en")));
    }

    @Test
    public void changeMailInvalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + faker.internet().emailAddress() + "\"}")
                        .sessionAttr("userLogin", faker.name().username()))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.errorMsg", is("Invalid session! en")));
    }

    @Test
    public void invalidContent() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("text/html"))
                .andExpect(status().isUnsupportedMediaType());
    }
}