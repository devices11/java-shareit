package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findAllByRequestor_IdOrderByCreated(long id);

    Collection<ItemRequest> findAllByRequestorIdIsNot(long id);
}
