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

    public void addGymToUser(int id, Gym gym) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().addGym(gym);
        //registeredUser.get().addWorkoutsFromUsersGyms();
    }

    @Transactional
    public void addRegisteredUser(RegisteredUser rUser) {
        registeredUserRepository.save(rUser);
    }

    public RegisteredUser findUserByName(String userName) {
        return registeredUserRepository.findByUserName(userName).size()==0 ? null : registeredUserRepository.findByUserName(userName).get(0);
    }

    public String loginUser(String userName, String password) {
        return "Login successful as "+userName;
    }

    public void deleteAll(){
        registeredUserRepository.deleteAllInBatch();
    }
}
