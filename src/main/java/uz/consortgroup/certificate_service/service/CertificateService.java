package uz.consortgroup.certificate_service.service;

import org.springframework.http.ResponseEntity;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;

public interface CertificateService {
    ResponseEntity create(CreateCertificateReqDto req);
}
