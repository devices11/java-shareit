package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Comment comment;
    private Item item;

    @BeforeEach
    void setUp() {
        User author = User.builder()
                .name("John Wickus")
                .email("johnwickus@test.com")
                .build();
        User author2 = User.builder()
                .name("John Wickus 2")
                .email("johnwickus2@test.com")
                .build();
        userRepository.save(author);
        userRepository.save(author2);

        item = Item.builder()
                .name("Test Item")
                .description("Test description")
                .available(true)
                .owner(author)
                .requestId(1L)
                .build();
        Item item2 = Item.builder()
                .name("Test Item 2")
                .description("Test description 2")
                .available(true)
                .owner(author2)
                .requestId(2L)
                .build();
        itemRepository.save(item);
        itemRepository.save(item2);

        comment = Comment.builder()
                .text("test comment")
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
        Comment comment2 = Comment.builder()
                .text("test comment 2")
                .item(item2)
                .author(author2)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
        commentRepository.save(comment2);
    }

    @Test
    void findAllByOwner_Id() {
        Collection<Comment> foundComments = commentRepository.findByItem_Id(item.getId());

        assertEquals(1, foundComments.size());
        assertTrue(foundComments.contains(comment));
    }
}
