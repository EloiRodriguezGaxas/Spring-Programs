package cat.tecnocampus.useCases;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.exceptions.UserLabNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserUseCasesTests {

	@Autowired
	private UserUseCases userUseCases;

	@Test(expected = DuplicateKeyException.class)
	public void testDuplicateUserLab() {
		UserLab u = new UserLab("jrjrjr", "pepe", "popo", "mail");
		
		IntStream.range(1, 5)
				 .mapToObj(i -> new NoteLab("hola " + i, "content " + i, LocalDateTime.now(), LocalDateTime.now()))
				 .forEach(u::addNote);
		
		userUseCases.saveUser(u);
	}

	@Test
	public void saveDeleteUser() {
		UserLab u = new UserLab("TestTest", "TestTest", "TestTest", "TestTest");

		userUseCases.saveUser(u);

		assertEquals(userUseCases.getUser("TestTest").getUsername(), "TestTest");

		assertEquals(userUseCases.deleteUser("TestTest"),1);

	}

	@Test(expected = UserLabNotFoundException.class)
	public void userDoesNotExists() {
		UserLab u = new UserLab("TestTest", "TestTest", "TestTest", "TestTest");

		userUseCases.getUser("TestTest");
	}
}
