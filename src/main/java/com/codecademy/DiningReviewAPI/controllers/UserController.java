package com.codecademy.DiningReviewAPI.controllers;

import com.codecademy.DiningReviewAPI.entities.User;
import com.codecademy.DiningReviewAPI.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequestMapping("api/v1/users")
@RestController
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("/user/{username}")
    public User getUserByName(@PathVariable("username") String userName){
        Optional<User> userOptional = this.userRepository.findByNameIgnoreCase(userName);
        if (!userOptional.isPresent() || !this.userRepository.existsUserByNameIgnoreCase(userName)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User newUser) {
        if (ObjectUtils.isEmpty(newUser.getName()) || this.userRepository.existsUserByNameIgnoreCase(newUser.getName())){
            throw new ResponseStatusException((HttpStatus.BAD_REQUEST));
        }
        return this.userRepository.save(newUser);
    }

    @PatchMapping(path ="/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable("username") String userName, @RequestBody JsonPatch patch){
        Optional<User> userToUpdateOptional = this.userRepository.findByNameIgnoreCase(userName);
        if (!userToUpdateOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User userToUpdate = userToUpdateOptional.get();
        User userPatched = applyPatchToUser(patch, userToUpdate);
        return this.userRepository.save(userPatched);

//        if (user.getCity()!=null){
//            userToUpdate.setCity(user.getCity());
//        }
//
//        if (user.getState()!=null){
//            userToUpdate.setState(user.getState());
//        }
//
//        if (user.getCode()!=null){
//            userToUpdate.setCode(user.getCode());
//        }
//
//        if (user.getPeanutAllergy()!=null){
//            userToUpdate.setPeanutAllergy(user.getPeanutAllergy());
//        }
//
//        if (user.getEggAllergy()!=null){
//            userToUpdate.setEggAllergy(user.getEggAllergy());
//        }
//
//        if (user.getDairyAllergy()!=null){
//            userToUpdate.setDairyAllergy(user.getDairyAllergy());
//        }
//        return this.userRepository.save(userToUpdate);
    }

    private User applyPatchToUser(JsonPatch patch, User targetUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = null;
        try {
            patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
            return objectMapper.treeToValue(patched,User.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            // TODO: 23/02/17 find a better way to handle this
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteUser(@PathVariable("id") Long id) {
        Optional<User> userToDeleteOptional = this.userRepository.findById(id);
        if (!userToDeleteOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User userToDelete = userToDeleteOptional.get();
        this.userRepository.delete(userToDelete);
        return userToDelete;

    }
}
