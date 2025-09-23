package uz.consortgroup.certificate_service.service.impl;

import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;
import uz.consortgroup.certificate_service.dto.CertificateData;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.dto.CreateCertificateResDto;
import uz.consortgroup.certificate_service.entity.Certificate;
import uz.consortgroup.certificate_service.repository.CertificateRepository;
import uz.consortgroup.certificate_service.service.CertificateService;
import uz.consortgroup.certificate_service.service.CourseService;
import uz.consortgroup.certificate_service.service.FileService;
import uz.consortgroup.certificate_service.service.ListenerService;
import uz.consortgroup.core.api.v1.dto.course.response.course.CourseResponseDto;
import uz.consortgroup.core.api.v1.dto.course.response.course.CourseTranslationResponseDto;
import uz.consortgroup.core.api.v1.dto.user.response.UserShortInfoResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final FileService fileService;
    private final CourseService courseService;
    private final ListenerService listenerService;
    private final CertificateRepository repository;

    public CertificateServiceImpl(FileService fileService, CourseService courseService, ListenerService listenerService,
                                  CertificateRepository repository) {
        this.fileService = fileService;
        this.courseService = courseService;
        this.listenerService = listenerService;
        this.repository = repository;
    }

    @Override
    public CreateCertificateResDto create(CreateCertificateReqDto req) {
        UserShortInfoResponseDto listener = listenerService.getListenerShortInfo(req.getListenerId());
        CourseResponseDto course = courseService.getCourseTitleById(req.getCourseId());

        String fullName = String.join(" ", listener.getFirstName(), listener.getLastName(), listener.getMiddleName());
        String courseName = course.getTranslations().stream().filter(Objects::nonNull).findFirst()
                .map(CourseTranslationResponseDto::getTitle).toString().toUpperCase();

        CertificateTemplate certificateTemplate = colorDefinitionCertificate(req.getScore());
        //Генерим ID для сертификата, он и будет серийный номер
        UUID certId = UUID.randomUUID();
        String serialNumber = generateSerialNumber(certId);
        String issuedDate = generateIssuedDate();
        String uploadPath = generateFileName(fullName, issuedDate);

        CertificateData data = CertificateData.builder()
                .fullName(fullName)
                .courseName(courseName)
                .score(req.getScore())
                .serialNumber(serialNumber)
                .issuedDate(issuedDate)
                .uploadPath(uploadPath)
                .certificateTemplate(certificateTemplate)
                .build();

        byte[] bytes = fileService.generateCertificate(data, certId);

        repository.save(Certificate.builder()
                        .id(certId)
                        .certificateTemplate(certificateTemplate)
                        .score(req.getScore())
                        .listenerId(req.getListenerId())
                        .courseId(req.getCourseId())
                        .uploadPath(uploadPath)
                .build());

        return null;

        //Нужно добавить логику чтоб обозначить что сертификат не создался.
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

    private String generateFileName(String fullName, String issuedDate){
        return String.join("", fullName, issuedDate, ".pdf");
    }

    private String generateIssuedDate(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


}
