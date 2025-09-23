package uz.consortgroup.certificate_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CertificateData {
    private String fullName;
    private String courseName;
    private Double score;
    private String serialNumber;
    private String issuedDate;
    private String uploadPath;
    private CertificateTemplate certificateTemplate;
}
