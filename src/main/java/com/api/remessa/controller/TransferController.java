package com.api.remessa.controller;

import com.api.remessa.dto.request.CreateTransferRequest;
import com.api.remessa.dto.response.TransferResponse;
import com.api.remessa.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse transfer(@RequestBody @Valid CreateTransferRequest request) {

        return transferService.transfer(request);
    }
}
