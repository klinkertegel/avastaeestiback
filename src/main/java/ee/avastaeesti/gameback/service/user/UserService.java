package ee.avastaeesti.gameback.service.user;

import ee.avastaeesti.gameback.controller.user.dto.UserDto;
import ee.avastaeesti.gameback.infrastructure.exception.ForbiddenException;
import ee.avastaeesti.gameback.persistence.role.Role;
import ee.avastaeesti.gameback.persistence.role.RoleRepository;
import ee.avastaeesti.gameback.persistence.user.User;
import ee.avastaeesti.gameback.persistence.user.UserMapper;
import ee.avastaeesti.gameback.persistence.user.UserRepository;
import ee.avastaeesti.gameback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ee.avastaeesti.gameback.infrastructure.Error.EMAIL_EXISTS;
import static ee.avastaeesti.gameback.infrastructure.Error.USERNAME_EXISTS;
import static ee.avastaeesti.gameback.status.Status.DELETED;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    public void addNewUser(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ForbiddenException(USERNAME_EXISTS.getMessage(), USERNAME_EXISTS.getErrorCode());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ForbiddenException(EMAIL_EXISTS.getMessage(), EMAIL_EXISTS.getErrorCode());
        }
        Role role = roleRepository.getReferenceById(2);
        User user = userMapper.toUser(userDto);
        user.setRole(role);
        userRepository.save(user);
    }

    public UserDto getUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("UserId", userId));
        UserDto userForEdit = userMapper.toUserDto(user);
        return userForEdit;
    }

    public void updateUser(Integer userId, UserDto userDto) {
        User userForEdit = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("UserId", userId));
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ForbiddenException(USERNAME_EXISTS.getMessage(), USERNAME_EXISTS.getErrorCode());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ForbiddenException(EMAIL_EXISTS.getMessage(), EMAIL_EXISTS.getErrorCode());
        }
        userMapper.updateUser(userDto, userForEdit);
        userRepository.save(userForEdit);
    }

    public void removeUser(Integer userId) {
        User userToRemove = userRepository.findById(userId).orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("UserId", userId));
        userToRemove.setStatus(DELETED.getCode());
        userRepository.save(userToRemove);
    }
}




