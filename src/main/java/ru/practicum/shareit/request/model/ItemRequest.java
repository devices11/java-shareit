package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class ItemRequest {
    private  Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
