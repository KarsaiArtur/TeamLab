package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.RatingRepository;
import tracker.repository.RegisteredUserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;
    private RatingRepository ratingRepository;


    @Transactional
    public void addRegisteredUser(RegisteredUser rUser) {
        registeredUserRepository.save(rUser);
    }

    public RegisteredUser findUserByName(String userName) {
        return registeredUserRepository.findByUserName(userName).size()==0 ? null : registeredUserRepository.findByUserName(userName).get(0);
    }

    public String loginUser(String userName, String password) {
        String message = registeredUserRepository.findByUserName(userName).size()==0 ? "Login failed: no User with such username" : "Login successful as "+userName;
        return message;
    }

    public void deleteAll(){
        registeredUserRepository.deleteAllInBatch();
    }
}
