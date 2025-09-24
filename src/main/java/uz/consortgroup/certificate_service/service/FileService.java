package uz.consortgroup.certificate_service.service;

import uz.consortgroup.certificate_service.dto.CertificateDto;

import java.util.UUID;

public interface FileService {
    boolean generateCertificate(CertificateDto certificateDto, UUID certId);
}
