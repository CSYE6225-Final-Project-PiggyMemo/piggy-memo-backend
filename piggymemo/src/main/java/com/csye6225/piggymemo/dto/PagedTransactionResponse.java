package com.csye6225.piggymemo.dto;

import java.util.List;

public record PagedTransactionResponse(
    List<TransactionResponse> records, int currentPage, int totalPages, long totalRecords)
{}
