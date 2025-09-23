package uz.consortgroup.certificate_service.dto;

import uz.consortgroup.certificate_service.constant.CertificateTemplate;

public class CreateCertificateResDto {
    private String listener;
    private String course;
    private Long certificateId;
    private String certificateSerialNumber;
    private CertificateTemplate certificateTemplate;
}
