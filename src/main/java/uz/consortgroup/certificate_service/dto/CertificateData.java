package uz.consortgroup.certificate_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateData {
    private String fullName;
    private String courseName;
    private String listenerId;
    private String courseId;
    private Double score;
}
