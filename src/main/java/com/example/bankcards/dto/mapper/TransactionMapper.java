package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.PagedResponse;
import com.example.bankcards.dto.transaction.TransactionResponse;
import com.example.bankcards.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(config = MapStructConfig.class)
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction tr);

    @Mapping(target = "page", source = "number")
    PagedResponse<TransactionResponse> toPagedResponse(Page<Transaction> pageTrs);
}
