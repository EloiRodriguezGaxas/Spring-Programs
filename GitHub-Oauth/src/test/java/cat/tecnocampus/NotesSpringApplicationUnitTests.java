package cat.tecnocampus;

import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.domain.UserLabBuilder;
import cat.tecnocampus.security.SecurityService;
import cat.tecnocampus.security.UserSecurityRepository;
import cat.tecnocampus.useCases.UserUseCases;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by roure on 25/11/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotesSpringApplicationUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserUseCases mockService;

    @MockBean
    private UserSecurityRepository mockSecurityRepository;

    @Test
    @WithMockUser("sergi")
    public void findAll_users() throws Exception {
        UserLab ccccc = new UserLabBuilder()
                .name("ccccc")
                .secondname("ccccc")
                .username("ccccc")
                .email("ccccc")
                .createUserLab();

        UserLab ddddd = new UserLabBuilder()
                .name("ddddd")
                .secondname("ddddd")
                .username("ddddd")
                .email("ddddd")
                .createUserLab();

        when(mockService.getUsers()).thenReturn(Arrays.asList(ccccc, ddddd));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("userLabList", hasSize(2)))
                .andExpect(model().attribute("userLabList", hasItem(
                        allOf(
                                hasProperty("name", is("ddddd")),
                                hasProperty("username", is("ddddd")),
                                hasProperty("secondName", is("ddddd")),
                                hasProperty("email", is("ddddd"))
                        )
                )))
                .andExpect(model().attribute("userLabList", hasItem(
                        allOf(
                                hasProperty("name", is("ccccc")),
                                hasProperty("username", is("ccccc")),
                                hasProperty("secondName", is("ccccc")),
                                hasProperty("email", is("ccccc"))
                        )
                )));

        verify(mockService, times(1)).getUsers();
        verifyNoMoreInteractions(mockService);
    }

    @Test
    public void createuser_get() throws Exception {
        mockMvc.perform(get("/createuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("userform"))
                .andExpect(model().attributeExists("userLab"))
                .andExpect(model().attribute("userLab", new UserLab()));
    }

    @Test
    public void createuserFormHasErrors() throws Exception {
        mockMvc.perform(post("/createuser")
                .param("name", "serg")
                .param("username", "sergisergi")
                .param("secondname", "secondname")
                .param("email", "email@email"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("userform"));

        verifyZeroInteractions(mockService);
    }

    @Test
    public void createuserNoErrors() throws Exception {
        UserLab ddddd = new UserLabBuilder()
                .name("ddddd")
                .secondname("ddddd")
                .username("ddddd")
                .email("ddddd@ddddd")
                .password("ddddd")
                .createUserLab();


        when(mockService.registerUser(isA(UserLab.class))).thenReturn(1);

        mockMvc.perform(post("/createuser")
                .param("name", "ddddd")
                .param("username", "ddddd")
                .param("secondName", "ddddd")
                .param("email", "ddddd@ddddd")
                .param("password", "ddddd"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/{username}"))
                .andExpect(redirectedUrl("/users/ddddd?pepe=pepe"));

        //ArgumentCaptor<UserLab> formObjectArgument = ArgumentCaptor.forClass(UserLab.class);
        verify(mockService, times(1)).registerUser(ddddd);     //(formObjectArgument.capture());   //(ddddd);
        verifyNoMoreInteractions(mockService);

        verify(mockSecurityRepository, times(1)).save("ddddd", "ddddd");
        verifyNoMoreInteractions(mockSecurityRepository);
    }

}