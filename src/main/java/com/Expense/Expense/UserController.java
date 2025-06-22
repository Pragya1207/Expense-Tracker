package com.Expense.Expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.toDto(userService.createUser(userDto));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userName}")
    public UserDto getUserByUserName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }

    @GetMapping("/check-username/{userName}")
    public ResponseEntity<String> isUserNameTaken(@PathVariable String userName) {
        boolean isPresent= userService.isUserNameTaken(userName);
        if(isPresent==true){
            return new ResponseEntity<>("user name already exist",HttpStatus.OK);
        }
        return new ResponseEntity<>("userName not in use",HttpStatus.OK);
    }
}
