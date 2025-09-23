package uz.consortgroup.certificate_service.service;

import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.dto.CreateCertificateResDto;

public interface CertificateService {
    CreateCertificateResDto create(CreateCertificateReqDto req);
}
