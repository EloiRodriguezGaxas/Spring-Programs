package cat.tecnocampus.useCase;

import cat.tecnocampus.dataBaseRepository.UserRepository;
import cat.tecnocampus.domain.User;
import cat.tecnocampus.sourceBin.CustomOutputEventSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("userUseCases")
public class UserUseCases {
    
    private final UserRepository userRepository;

    private final CustomOutputEventSource output;

    public UserUseCases(UserRepository userLabRepository, CustomOutputEventSource output) {
        this.userRepository = userLabRepository;
        this.output = output;
    }

    public User createUser(String username, String name, String secondName, String email) {
        User user = new User(username, name, secondName, email);
        registerUser(user);
        return user;
    }

    //The @Transactiona annotation states that saveUser is a transaction. So ,if a unchecked exception is signaled
    // (and not cached) during the saveUser method the transaction is going to rollback
    @Transactional
    public int registerUser(User user) {

        return userRepository.save(user);
    }

    //The @Transactiona annotation states that saveUser is a transaction. So ,if a unchecked exception is signaled
    // (and not cached) during the saveUser method the transaction is going to rollback
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void deleteUser(String username) throws Exception{
        try {
            this.userRepository.delete(username);
            this.output.userToDelete(username);
        }catch (Exception e) {
            throw new Exception("User not found");
        }
    }

    //Note that users don't have their notes with them
    public List<User> getUsers() {
        return userRepository.findAllLazy();
    }

    public User getUser(String userName) {
        try {
            return userRepository.findOne(userName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
