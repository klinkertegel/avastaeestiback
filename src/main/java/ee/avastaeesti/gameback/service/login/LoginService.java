package ee.avastaeesti.gameback.service.login;

import ee.avastaeesti.gameback.controller.login.dto.LoginResponse;
import ee.avastaeesti.gameback.infrastructure.exception.ForbiddenException;
import ee.avastaeesti.gameback.persistence.user.User;
import ee.avastaeesti.gameback.persistence.user.UserMapper;
import ee.avastaeesti.gameback.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ee.avastaeesti.gameback.infrastructure.Error.INCORRECT_CREDENTIALS;
import static ee.avastaeesti.gameback.status.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public LoginResponse login(String username, String password) {
        User user = userRepository.findUserBy(username, password, ACTIVE.getCode()).orElseThrow(() -> new ForbiddenException(INCORRECT_CREDENTIALS.getMessage(), INCORRECT_CREDENTIALS.getErrorCode()));
        LoginResponse loginResponse = userMapper.toLoginResponse(user);
        return loginResponse;
    }
}
