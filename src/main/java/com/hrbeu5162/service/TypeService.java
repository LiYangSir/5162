package com.hrbeu5162.service;

import com.hrbeu5162.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TypeService {
    Type saveType(Type type);

    Type getTypeByName(String Name);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    Long countType();

    List<Type> listType(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);
}
