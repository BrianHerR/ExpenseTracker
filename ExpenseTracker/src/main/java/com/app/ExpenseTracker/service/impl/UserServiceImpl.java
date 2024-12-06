package com.app.ExpenseTracker.service.impl;
import com.app.ExpenseTracker.dto.request.UserRequestDto;
import com.app.ExpenseTracker.dto.response.UserResponseDto;
import com.app.ExpenseTracker.entity.User;
import com.app.ExpenseTracker.exception.UserNotFoundException;
import com.app.ExpenseTracker.repository.UserRepository;
import com.app.ExpenseTracker.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

     UserRepository userRepository;

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        log.info("se ingresa al servicio findUserById con el id: {}", id);

         return userRepository.findById(id).orElseThrow(UserNotFoundException::new);

     }

    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {

        log.info("se ingresa al servicio addUser con el request: {}", userRequestDto);

        User newUser = new User();
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setName(userRequestDto.getName());

        log.info("id del usuario a guardar: {}, {}, {}", newUser.getId(), userRequestDto.getName(), userRequestDto.getEmail());

        User user = userRepository.save(newUser);

        log.info("Se guarda el usuario: {}", user);

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
     }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) throws UserNotFoundException {

        log.info("se ingresa al servicio con el request: {} y el id: {}", userRequestDto, id);


         User existingUser = userRepository.findById(id)
                 .orElseThrow(UserNotFoundException::new);

        log.info("Usuario encontrado: {} con el id: {}", existingUser, id);

         existingUser.setName(userRequestDto.getName());
         existingUser.setEmail(userRequestDto.getEmail());

        log.info("Usuario editado: {} ", existingUser);

         return new UserResponseDto(id, existingUser.getName(), existingUser.getEmail());
     }

    @Override
    public void deleteUser(Long id) {
        log.info("se ingresa al servicio de DeleteUser con el id: {}", id);


        userRepository.deleteById(id);
    }


}
