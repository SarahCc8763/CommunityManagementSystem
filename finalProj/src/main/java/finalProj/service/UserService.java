package finalProj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.users.Users;
import finalProj.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Optional<Users> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Users save(Users entity) {
        return userRepository.save(entity);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
