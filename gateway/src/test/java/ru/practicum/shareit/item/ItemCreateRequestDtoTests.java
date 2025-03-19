package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemCreateRequestDtoTests {
    @Autowired
    private JacksonTester<ItemCreateRequestDto> jsonItemCreateRequestDto;

    @Test
    public void serialize() throws Exception {
        ItemCreateRequestDto itemCreateRequestDto = ItemCreateRequestDto.builder()
                .name("Test")
                .description("Test description")
                .available(true)
                .requestId(1L)
                .build();

        JsonContent<ItemCreateRequestDto> json = jsonItemCreateRequestDto.write(itemCreateRequestDto);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Test description");
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    public void deserialize() throws Exception {
        String jsonContent = """
                    \n{
                        "name": "Test",
                        "description": "Test description",
                        "available": true,
                        "requestId": 1
                    }
                """;

        ItemCreateRequestDto itemCreateRequestDto = jsonItemCreateRequestDto.parseObject(jsonContent);

        assertThat(itemCreateRequestDto.getName()).isEqualTo("Test");
        assertThat(itemCreateRequestDto.getDescription()).isEqualTo("Test description");
        assertThat(itemCreateRequestDto.getAvailable()).isEqualTo(true);
        assertThat(itemCreateRequestDto.getRequestId()).isEqualTo(1);
    }

    @Test
    public void validation() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        ItemCreateRequestDto commentRequestDto = ItemCreateRequestDto.builder()
                .name("")
                .description("")
                .available(null)
                .build();

        Set<ConstraintViolation<ItemCreateRequestDto>> violations = validator.validate(commentRequestDto);

        assertThat(violations).hasSize(3);
        assertThat(violations).extracting("message").contains("Название вещи не может быть пустым");
        assertThat(violations).extracting("message").contains("Описание вещи не может быть пустым");
        assertThat(violations).extracting("message").contains("Статус доступности не может быть null");
    }
}
