package com.Expense.Expense;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(UserDto dto) {
        User user = new User();
        user.setUserName(dto.userName);
        user.setEmailId(dto.emailId);

        if (dto.categories != null) {
            List<Category> categories = new ArrayList<>();
            for (CategoryDto catDto : dto.categories) {
                Category category = new Category();
                category.setName(catDto.name);
                category.setAmount(catDto.amount);

                List<Item> itemList = new ArrayList<>();
                if (catDto.items != null) {
                    for (ItemDto itemDto : catDto.items) {
                        Item item = new Item();
                        item.setItemName(itemDto.itemName);
                        item.setCost(itemDto.cost);
                        itemList.add(item);
                    }
                }
                category.setItems(itemList);
                categories.add(category);
            }
            user.setCategories(categories);
        }

        return userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDto getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userName: " + userName));
        return toDto(user);
    }

    public boolean isUserNameTaken(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.userName = user.getUserName();
        dto.emailId = user.getEmailId();
        if (user.getCategories() != null) {
            dto.categories = user.getCategories().stream().map(cat -> {
                CategoryDto cdto = new CategoryDto();
                cdto.name = cat.getName();
                cdto.amount = cat.getAmount();
                cdto.items = cat.getItems().stream().map(it -> {
                    ItemDto idto = new ItemDto();
                    idto.itemName = it.getItemName();
                    idto.cost = it.getCost();
                    return idto;
                }).collect(Collectors.toList());
                return cdto;
            }).collect(Collectors.toList());
        }
        return dto;
    }

}
