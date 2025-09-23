package uz.consortgroup.certificate_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.service.CertificateService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping("/create")
    @Operation(summary = "Create certificate admin role")
//    @PreAuthorize("hasAuthority('create certificate')")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateCertificateReqDto dto) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(certificateService.create(dto));
    }

}
