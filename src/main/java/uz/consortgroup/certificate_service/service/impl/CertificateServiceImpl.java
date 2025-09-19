package uz.consortgroup.certificate_service.service.impl;

import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.service.CertificateService;
import uz.consortgroup.certificate_service.service.FileService;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final FileService fileService;

    public CertificateServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public ResponseEntity create(CreateCertificateReqDto req){
        try {
            byte[] certificatePdf = fileService.generateCertificate(req);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(certificatePdf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
