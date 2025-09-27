package uz.consortgroup.certificate_service.service;

import org.springframework.core.io.FileSystemResource;
import uz.consortgroup.certificate_service.dto.CertificateDto;
import uz.consortgroup.certificate_service.entity.Certificate;

import java.util.UUID;

public interface FileService {
    boolean generateCertificate(CertificateDto certificateDto, UUID certId);

    FileSystemResource download(Certificate certificate);
}
