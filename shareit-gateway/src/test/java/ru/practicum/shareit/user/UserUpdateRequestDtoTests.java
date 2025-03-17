package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserUpdateRequestDtoTests {

    @Autowired
    private JacksonTester<UserUpdateRequestDto> jsonUserUpdateRequestDto;

    @Test
    public void serialize() throws Exception {
        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();

        JsonContent<UserUpdateRequestDto> json = jsonUserUpdateRequestDto.write(userUpdateRequestDto);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John Wick");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("johnwick@test.com");
    }

    @Test
    public void deserialize() throws Exception {
        String jsonContent = """
                    {
                        "name": "John Wick",
                        "email": "johnwick@test.com"
                    }
                """;

        UserUpdateRequestDto userUpdateRequestDto = jsonUserUpdateRequestDto.parseObject(jsonContent);

        assertThat(userUpdateRequestDto.getName()).isEqualTo("John Wick");
        assertThat(userUpdateRequestDto.getEmail()).isEqualTo("johnwick@test.com");
    }

    @Test
    public void validation() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .email("invalid-email")
                .build();

        Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(userUpdateRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").contains("Некорректный формат email");
    }
}
