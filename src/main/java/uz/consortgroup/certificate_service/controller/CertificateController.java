package uz.consortgroup.certificate_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.consortgroup.certificate_service.dto.CertificateDto;
import uz.consortgroup.certificate_service.dto.CertificateFilter;
import uz.consortgroup.certificate_service.dto.req.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.service.CertificateService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping("/create")
    @Operation(summary = "Generate certificate")
//    @PreAuthorize("hasAuthority('create certificate')")
    public ResponseEntity<CertificateDto> create(@Valid @RequestBody CreateCertificateReqDto dto) {
        return ResponseEntity.ok().body(certificateService.create(dto));
    }

    @GetMapping("{id}/download")
    @Operation(summary = "Download certificate")
    public ResponseEntity<Object> downloadById(@PathVariable UUID id) {
        return certificateService.downloadById(id);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCertificates(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String certificateColor,
            @RequestParam(required = false) Instant issuedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        CertificateFilter filter = new CertificateFilter(fullName, courseName, certificateColor, issuedAt);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("issuedDate").descending());

        Page<CertificateDto> result = certificateService.getCertificates(filter, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("items", result.getContent());
        response.put("total", result.getTotalElements());
        response.put("page", result.getNumber());
        response.put("pages", result.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
