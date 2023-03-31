package com.example.demo.list.converter;

import com.example.demo.list.entity.ListEntity;
import com.example.demo.list.models.DetailedListDto;
import com.example.demo.list.models.ShortListDto;

public interface ListConverter {
    DetailedListDto convert(ListEntity entity);

    ListEntity convert(ShortListDto dto);

    ShortListDto convertToShort(ListEntity listEntity);
}
