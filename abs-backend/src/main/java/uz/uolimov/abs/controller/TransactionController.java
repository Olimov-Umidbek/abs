package uz.uolimov.abs.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uolimov.abs.model.dto.transaction.StatusRequestDTO;
import uz.uolimov.commons.model.dto.ErrorDTO;
import uz.uolimov.abs.model.dto.transaction.ExecuteResponseDTO;
import uz.uolimov.abs.model.dto.transaction.ExecuteRequestDTO;
import uz.uolimov.abs.model.dto.transaction.TransactionDetailsDTO;
import uz.uolimov.abs.service.TransactionDetailsService;
import uz.uolimov.abs.service.TransactionService;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad request",
        content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
        }
    ),
    @ApiResponse(responseCode = "500", description = "Internal server error",
        content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
        }
    )
})
public class TransactionController {

    private final TransactionService service;
    private final TransactionDetailsService detailsService;

    @Operation(
        summary = "Endpoint to execute transaction",
        description = "Respond transaction status"
    )
    @ApiResponse(responseCode = "200", description = "Successful",
        content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExecuteResponseDTO.class))
        }
    )
    @PostMapping("/execute")
    public ResponseEntity<ExecuteResponseDTO> execute(
        @RequestBody ExecuteRequestDTO request,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(
            service.execute(request, userId)
        );
    }

    @PostMapping("/status")
    public ResponseEntity<TransactionDetailsDTO> status(
        @RequestBody StatusRequestDTO request,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(
            service.status(request, userId)
        );
    }

    @Operation(
        summary = "Endpoint to retrieve transaction details",
        description = "Return transaction details"
    )
    @ApiResponse(responseCode = "200", description = "Successful",
        content = { @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TransactionDetailsDTO.class)
        )}
    )
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDetailsDTO> getOne(
        @PathVariable UUID transactionId,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(
            detailsService.getOne(transactionId, userId)
        );
    }

    @Operation(
        summary = "Endpoint to retrieve all transaction details for specific user",
        description = "Return transaction details"
    )
    @ApiResponse(responseCode = "200", description = "Successful")
    @GetMapping("/by-user")
    public Page<TransactionDetailsDTO> getAllByUserId(
        @RequestHeader("X-User-Id") UUID userId,
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "sortBy", required = false) String sortBy
    ) {
        if(null == offset) offset = 0;
        if(null == pageSize) pageSize = 10;
        if(StringUtils.isEmpty(sortBy)) sortBy ="status";

        return detailsService.getAllByUserId(userId, PageRequest.of(offset, pageSize, Sort.by(sortBy)));
    }

    @Operation(
        summary = "Endpoint to retrieve transaction details",
        description = "Return transaction details"
    )
    @ApiResponse(responseCode = "200", description = "Successful")
    @GetMapping("/by-range")
    public Page<TransactionDetailsDTO> getAllByDateRange(
        @RequestHeader("X-User-Id") UUID userId,
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("startDate") LocalDate endDate,
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "sortBy", required = false) String sortBy
        ) {
        if(null == offset) offset = 0;
        if(null == pageSize) pageSize = 10;
        if(StringUtils.isEmpty(sortBy)) sortBy ="status";

        return detailsService.getAllByDateRange(
            userId,
            startDate,
            endDate,
            PageRequest.of(offset, pageSize, Sort.by(sortBy))
        );
    }
}
