package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findById(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> findAllForOwner(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> findByText(String text) {
        return get("?text=" + text);
    }

    public ResponseEntity<Object> create(Long ownerId, ItemCreateRequestDto itemCreateRequestDto) {
        return post("", ownerId, itemCreateRequestDto);
    }

    public ResponseEntity<Object> update(Long ownerId, Long id, ItemUpdateRequestDto itemUpdateRequestDto) {
        return patch("/" + id, ownerId, itemUpdateRequestDto);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentRequestDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
