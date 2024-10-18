package ru.luckyskeet.usermanagment.service;

import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.model.UserDtoResponse;
import ru.luckyskeet.usermanagment.model.UserDtoToUpdate;

import java.util.List;

public interface UserService {

    UserDtoResponse createUser(UserDtoRequest userDto);

    UserDtoResponse updateUserById(Long userId, UserDtoToUpdate userDtoToUpdate);

    void deleteUserById(Long userId);

    UserDtoResponse getUserById(Long id);

    List<UserDtoResponse> getUsers(Integer from, Integer size);

}
