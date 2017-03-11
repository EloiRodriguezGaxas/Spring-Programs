package cat.tecnocampus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotesSpringApplicationSecurityTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void contextLoads() throws Exception {
		mockMvc.perform(get("/createuser"))
		.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testWithAnonymousUser() throws Exception {
		mockMvc.perform(get("/users/xxxxxx"))
		.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithMockUser("sergi")
	public void testWithInvalidUser() throws Exception {
		mockMvc.perform(get("/users/xxxxxx"))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithXXXXXXUser
	public void testWithValidUser() throws Exception {
		mockMvc.perform(get("/users/xxxxxx"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("userLab"));
	}
}