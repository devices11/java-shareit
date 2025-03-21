package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwner_Id(long userId);

    Collection<Item> findAllByRequestIdIn(Collection<Long> requestIds);

    Collection<Item> findAllByRequestId(Long requestIds);

    @Query("""
            SELECT i FROM Item i
            WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%'))
                OR (LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))))
                AND i.available = true""")
    Collection<Item> searchAvailableItemsIgnoreCase(@Param("text") String text);
}
