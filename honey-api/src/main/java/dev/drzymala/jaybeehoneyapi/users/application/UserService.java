package dev.drzymala.jaybeehoneyapi.users.application;

import dev.drzymala.jaybeehoneyapi.users.application.port.UserRegistrationUseCase;
import dev.drzymala.jaybeehoneyapi.users.db.UserEntityRepository;
import dev.drzymala.jaybeehoneyapi.users.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserService implements UserRegistrationUseCase {

    private final UserEntityRepository repository;

    @Transactional
    @Override
    public RegisterResponse register(String username, String password) {

        if (repository.findByUsernameIgnoreCase(username).isPresent()) {
            return RegisterResponse.failure("Account already exists");
        }

        UserEntity entity = new UserEntity(username, password);
        return RegisterResponse.success(repository.save(entity));
    }
}
