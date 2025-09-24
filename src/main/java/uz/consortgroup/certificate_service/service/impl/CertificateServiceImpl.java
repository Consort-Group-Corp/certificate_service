package uz.consortgroup.certificate_service.service.impl;

import jakarta.ws.rs.core.HttpHeaders;
import org.apache.commons.collections.list.TransformedList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;
import uz.consortgroup.certificate_service.dto.CertificateDto;
import uz.consortgroup.certificate_service.dto.CertificateFilter;
import uz.consortgroup.certificate_service.dto.req.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.entity.Certificate;
import uz.consortgroup.certificate_service.exception.CertificateNotFoundException;
import uz.consortgroup.certificate_service.mapper.CertificateMapper;
import uz.consortgroup.certificate_service.repository.CertificateRepository;
import uz.consortgroup.certificate_service.service.CertificateService;
import uz.consortgroup.certificate_service.service.CourseService;
import uz.consortgroup.certificate_service.service.FileService;
import uz.consortgroup.certificate_service.service.ListenerService;
import uz.consortgroup.certificate_service.service.specification.CertificateSpecification;
import uz.consortgroup.core.api.v1.dto.course.enumeration.CourseStatus;
import uz.consortgroup.core.api.v1.dto.course.response.course.CourseResponseDto;
import uz.consortgroup.core.api.v1.dto.course.response.course.CourseTranslationResponseDto;
import uz.consortgroup.core.api.v1.dto.user.enumeration.UserRole;
import uz.consortgroup.core.api.v1.dto.user.response.UserShortInfoResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final FileService fileService;
    private final CourseService courseService;
    private final ListenerService listenerService;
    private final CertificateRepository repository;
    private final CertificateMapper certificateMapper;

    public CertificateServiceImpl(FileService fileService, CourseService courseService, ListenerService listenerService,
                                  CertificateRepository repository, CertificateMapper certificateMapper) {
        this.fileService = fileService;
        this.courseService = courseService;
        this.listenerService = listenerService;
        this.repository = repository;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public CertificateDto create(CreateCertificateReqDto req) {
//        UserShortInfoResponseDto listener = listenerService.getListenerShortInfo(req.getListenerId());
//        CourseResponseDto course = courseService.getCourseTitleById(req.getCourseId());

        UserShortInfoResponseDto listener = new UserShortInfoResponseDto(UUID.randomUUID(), "Ali",
                "Aliyev", "Otasi ismi", UserRole.STUDENT, "student");
        CourseResponseDto course = CourseResponseDto.builder()
                .id(UUID.randomUUID())
                .courseStatus(CourseStatus.ACTIVE)
                .translations(List.of(new CourseTranslationResponseDto(UUID.randomUUID(), null, "My first course title", null, null)))
                .build();

        String fullName = String.join(" ", listener.getFirstName(), listener.getLastName(), listener.getMiddleName());
        String courseName = course.getTranslations().stream().filter(Objects::nonNull).findFirst()
                .map(CourseTranslationResponseDto::getTitle).toString().toUpperCase();

        CertificateTemplate certificateTemplate = colorDefinitionCertificate(req.getScore());
        //Генерим ID для сертификата, он и будет серийный номер
        UUID certId = UUID.randomUUID();
        String serialNumber = generateSerialNumber(certId);
        String issuedDate = generateIssuedDate();
        String uploadPath = "certificates/" + generateFileName(certId.toString());

        CertificateDto dto = CertificateDto.builder()
                .certId(certId)
                .serialNumber(serialNumber)
                .score(req.getScore())
                .listenerId(req.getListenerId())
                .listenerFullName(fullName)
                .courseId(req.getCourseId())
                .courseName(courseName)
                .issuedDate(issuedDate)
                .uploadPath(uploadPath)
                .certificateTemplate(certificateTemplate)
                .build();

        boolean success = fileService.generateCertificate(dto, certId);
        if (success) {
            Certificate entity = certificateMapper.toEntity(dto);
            repository.save(entity);
            return dto;
        }
        return null;
    }

    @Override
    public ResponseEntity<Object> downloadById(UUID id) {
        Certificate certificate = repository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException("Certificate not found by id:" + id));

        String uploadPath = certificate.getUploadPath();
        try {
            ClassPathResource resource = new ClassPathResource(uploadPath);
            if (!resource.exists()) {
                throw new CertificateNotFoundException("File not found: " + uploadPath);
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading certificate", e);
        }
    }

    @Override
    public Page<CertificateDto> getCertificates(CertificateFilter filter, Pageable pageable) {
        Page<Certificate> page = repository.findAll(CertificateSpecification.withFilters(filter), pageable);

        return page.map(certificateMapper::toDto);
    }

    private CertificateTemplate colorDefinitionCertificate(Double score) {
        if (score == null) {
            return CertificateTemplate.UNKNOWN;
        }
        if (score >= 80.0) {
            return CertificateTemplate.GREEN;
        } else if (score >= 50.0) {
            return CertificateTemplate.BLUE;
        } else {
            return CertificateTemplate.RED;
        }
    }

    private String generateSerialNumber(UUID certId) {
        return "AA-" + certId.toString().substring(0, 8).toUpperCase();
    }

    private String generateFileName(String certId) {
        return String.join("", certId, ".pdf");
    }

    private String generateIssuedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


}
