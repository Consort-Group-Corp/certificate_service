package uz.consortgroup.certificate_service.service;

import uz.consortgroup.certificate_service.dto.CertificateData;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;

import java.util.UUID;

public interface FileService {
    byte[] generateCertificate(CertificateData certificateData, UUID certId);
}
