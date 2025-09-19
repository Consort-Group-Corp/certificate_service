package uz.consortgroup.certificate_service.service;

import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;

public interface FileService {
    byte[] generateCertificate(CreateCertificateReqDto certificateRequest);
}
