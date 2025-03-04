package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "created", source = "created")
    CommentResponseDto toCommentResponseDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentRequestDto.text")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(CommentRequestDto commentRequestDto, Item item, User user);
}