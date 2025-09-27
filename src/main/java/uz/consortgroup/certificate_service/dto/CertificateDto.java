package uz.consortgroup.certificate_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CertificateDto {
    private UUID certId;
    private String serialNumber;
    private Double score;
    private UUID listenerId;
    private String listenerFullName;
    private UUID courseId;
    private String courseName;
    private String issuedDate;
    private String uploadPath;
    private CertificateTemplate certificateTemplate;
}
