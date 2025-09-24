package uz.consortgroup.certificate_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import uz.consortgroup.certificate_service.dto.CertificateDto;
import uz.consortgroup.certificate_service.dto.CertificateFilter;
import uz.consortgroup.certificate_service.dto.req.CreateCertificateReqDto;

import java.util.UUID;

public interface CertificateService {
    CertificateDto create(CreateCertificateReqDto req);
    ResponseEntity<Object> downloadById(UUID id);

    Page<CertificateDto> getCertificates(CertificateFilter filter, Pageable pageable);
}
