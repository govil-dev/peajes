package co.quind.peajes.tollcollection.application.dto;

import java.util.List;

public record TransactionHistoryResponse(
    List<TransactionHistoryItem> content,
    long totalElements,
    int totalPages,
    int pageNumber,
    int pageSize
) {
    public static TransactionHistoryResponse empty(int page, int size) {
        return new TransactionHistoryResponse(List.of(), 0L, 0, page, size);
    }
}
