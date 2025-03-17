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
import ru.practicum.shareit.item.dto.CommentRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentRequestDtoTests {
    @Autowired
    private JacksonTester<CommentRequestDto> jsonCommentRequestDto;

    @Test
    public void serialize() throws Exception {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("John Wick buried the dog")
                .build();

        JsonContent<CommentRequestDto> json = jsonCommentRequestDto.write(commentRequestDto);

        assertThat(json).extractingJsonPathStringValue("$.text")
                .isEqualTo("John Wick buried the dog");
    }

    @Test
    public void deserialize() throws Exception {
        String jsonContent = """
                \n{
                    "text":"John Wick buried the dog"
                }
                """;

        CommentRequestDto commentRequestDto = jsonCommentRequestDto.parseObject(jsonContent);

        assertThat(commentRequestDto.getText()).isEqualTo("John Wick buried the dog");
    }

    @Test
    public void validation() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("")
                .build();

        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").contains("Комментарий не может быть пустым");
    }
}
