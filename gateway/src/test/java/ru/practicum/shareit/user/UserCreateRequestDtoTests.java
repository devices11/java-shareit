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
import ru.practicum.shareit.user.dto.UserCreateRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserCreateRequestDtoTests {
    @Autowired
    private JacksonTester<UserCreateRequestDto> jsonUserCreateRequestDto;

    @Test
    public void serialize() throws Exception {
        UserCreateRequestDto userCreateRequestDto = UserCreateRequestDto.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();

        JsonContent<UserCreateRequestDto> json = jsonUserCreateRequestDto.write(userCreateRequestDto);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John Wick");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("johnwick@test.com");
    }

    @Test
    public void deserialize() throws Exception {
        String jsonContent = """
                    \n{
                        "name": "John Wick",
                        "email": "johnwick@test.com"
                    }
                """;

        UserCreateRequestDto userCreateRequestDto = jsonUserCreateRequestDto.parseObject(jsonContent);

        assertThat(userCreateRequestDto.getName()).isEqualTo("John Wick");
        assertThat(userCreateRequestDto.getEmail()).isEqualTo("johnwick@test.com");
    }

    @Test
    public void validation() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        UserCreateRequestDto userCreateRequestDto = UserCreateRequestDto.builder()
                .name("")
                .email("invalid-email")
                .build();

        Set<ConstraintViolation<UserCreateRequestDto>> violations = validator.validate(userCreateRequestDto);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").contains(
                "Имя пользователя не может быть пустым",
                "Некорректный формат email"
        );
    }
}
