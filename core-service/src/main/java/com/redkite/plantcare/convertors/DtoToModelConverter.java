package com.redkite.plantcare.convertors;


import java.util.List;
import java.util.stream.Collectors;

public interface DtoToModelConverter<M, T> {

    M toModel(T dto);

    default List<M> toModelList(List<T> items) {
        return items.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
