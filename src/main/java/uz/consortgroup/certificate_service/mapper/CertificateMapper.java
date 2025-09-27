package uz.consortgroup.certificate_service.mapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import uz.consortgroup.certificate_service.dto.CertificateDto;
import uz.consortgroup.certificate_service.entity.Certificate;

@Log4j2
@Component
public class CertificateMapper {
    public Certificate toEntity(CertificateDto dto) {
        return Certificate.builder()
                .id(dto.getCertId())
                .serialNumber(dto.getSerialNumber())
                .score(dto.getScore())
                .listenerId(dto.getListenerId())
                .listenerFullName(dto.getListenerFullName())
                .courseId(dto.getCourseId())
                .courseName(dto.getCourseName())
                .uploadPath(dto.getUploadPath())
                .certificateTemplate(dto.getCertificateTemplate())
                .build();
    }

    public CertificateDto toDto(Certificate entity) {
        return CertificateDto.builder()
                .certId(entity.getId())
                .serialNumber(entity.getSerialNumber())
                .score(entity.getScore())
                .listenerId(entity.getListenerId())
                .listenerFullName(entity.getListenerFullName())
                .courseId(entity.getCourseId())
                .courseName(entity.getCourseName())
                .uploadPath(entity.getUploadPath())
                .certificateTemplate(entity.getCertificateTemplate())
                .build();
    }
}
