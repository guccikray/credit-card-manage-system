package org.guccikray.creditcardmanagesystem.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Абстрактный класс который содержит в себе базовую логику преобразования сущности в модель
 * @param <T> модель
 * @param <E> сущность
 */
public abstract class AbstractModelAssembler<T, E> {

    public abstract T toModel(@NotNull E entity);

    public List<T> toModels(@NotNull List<E> entities) {
        return entities.stream()
            .map(this::toModel)
            .toList();
    }

    public Page<T> toPagedModel(@NotNull Page<E> page) {
        List<E> entities = page.getContent();

        return new PageImpl<>(
            toModels(entities),
            page.getPageable(),
            page.getTotalElements()
        );
    }
}
