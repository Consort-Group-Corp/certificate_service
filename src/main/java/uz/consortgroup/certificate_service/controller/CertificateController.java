package uz.consortgroup.certificate_service.controller;

import jakarta.validation.Valid;
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
//    @PreAuthorize("hasAuthority('create certificate')")
    public ResponseEntity create(@Valid @RequestBody CreateCertificateReqDto dto) {
        return certificateService.create(dto);
    }

}
