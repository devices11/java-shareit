package ru.practicum.shareit.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestCreateDtoTests {
    @Autowired
    private JacksonTester<ItemRequestCreateDto> jsonItemRequestCreateDto;

    @Test
    public void serialize() throws Exception {
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .description("John Wick buried the dog")
                .build();

        JsonContent<ItemRequestCreateDto> json = jsonItemRequestCreateDto.write(itemRequestCreateDto);

        assertThat(json).extractingJsonPathStringValue("$.description")
                .isEqualTo("John Wick buried the dog");
    }

    @Test
    public void deserialize() throws Exception {
        String jsonContent = """
                {
                    "description":"John Wick buried the dog"
                }
                """;

        ItemRequestCreateDto itemRequestCreateDto = jsonItemRequestCreateDto.parseObject(jsonContent);

        assertThat(itemRequestCreateDto.getDescription()).isEqualTo("John Wick buried the dog");
    }

    @Test
    public void validation() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .description("")
                .build();

        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(itemRequestCreateDto);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").contains("Описание не может быть пустым");
    }
}
