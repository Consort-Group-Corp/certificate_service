package uz.consortgroup.certificate_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;
import uz.consortgroup.certificate_service.dto.CertificateData;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.service.CertificateService;
import uz.consortgroup.certificate_service.service.FileService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private final CertificateService certificateService;
    ResourceLoader resourceLoader = new DefaultResourceLoader();

    public FileServiceImpl(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    private final String reportsDir = "path/to/reports/directory"; // укажите путь к директории с шаблонами
    @Override
    public byte[] generateCertificate(CreateCertificateReqDto certificateRequest) {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            // Получаем данные слушателя и курса (заглушка - нужно реализовать получение реальных данных)
            CertificateData certificateData = getCertificateData(
                    certificateRequest.getListenerId(),
                    certificateRequest.getCourseId(),
                    certificateRequest.getScore()
            );

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                    Collections.singletonList(certificateData)
            );

            // Загружаем изображения для шаблона
            URL BACKGROUND_IMAGE_URL = this.getClass().getClassLoader().getResource(
                    "image/" + certificateRequest.getCertificateTemplate().getBackgroundImage()
            );
            URL LOGO_AGENCY_URL = this.getClass().getClassLoader().getResource("image/agency_logo.png");
            URL LOGO_ACADEMY_URL = this.getClass().getClassLoader().getResource("image/academy_logo.png");

            Map<String, Object> params = new HashMap<>();

            // Основные параметры
            params.put("BACKGROUND_IMAGE", BACKGROUND_IMAGE_URL);
            params.put("DIRECTOR_NAME", "A.E.Burvanov"); // можно получить из БД или конфига
            params.put("DEPUTY_NAME", "E.V.Kolenko"); // можно получить из БД или конфига
            params.put("CERTIFICATE_NUMBER", generateCertificateNumber());
            params.put("RECIPIENT_NAME", certificateData.getFullName());
            params.put("COURSE_NAME", certificateData.getCourseName());
            params.put("ISSUE_DATE", format);
            params.put("SCORE", certificateRequest.getScore());
            params.put("SCORE_TEXT", getScoreText(certificateRequest.getScore()));

            // Дополнительные параметры для QR кода и т.д.
            params.put("qrcode", generateQRCode(certificateData));
            params.put("currentDate", format);
            params.put("agencyLogo", LOGO_AGENCY_URL);
            params.put("academyLogo", LOGO_ACADEMY_URL);

            // Выбираем шаблон в зависимости от типа сертификата
            JasperReport jasperReport;
            CertificateTemplate template = certificateRequest.getCertificateTemplate();

            jasperReport = switch (template) {
                case RED -> JasperCompileManager.compileReport(
                        resourceLoader.getResource(reportsDir + File.separator + "RED.jrxml").getInputStream());
                case BLUE -> JasperCompileManager.compileReport(
                        resourceLoader.getResource(reportsDir + File.separator + "BLUE.jrxml").getInputStream());
                case GREEN -> JasperCompileManager.compileReport(
                        resourceLoader.getResource(reportsDir + File.separator + "GREEN.jrxml").getInputStream());
            };

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            ByteArrayOutputStream byteArrayOutputStream = exportPDF(jasperPrint);

            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating certificate", e);
        }
    }

    private CertificateData getCertificateData(String listenerId, String courseId, Double score) {
        // Реализуйте получение данных слушателя и курса из базы данных
        CertificateData data = new CertificateData();

        // Заглушка - замените реальными данными из БД
        data.setFullName("F.I.Sharipov");
        data.setCourseName("Korrupsiyaga qarshi kurash bo'yicha malaka oshirish kursi");
        data.setListenerId(listenerId);
        data.setCourseId(courseId);
        data.setScore(score);

        return data;
    }

    private String generateCertificateNumber() {
        // Генерация уникального номера сертификата
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String getScoreText(Double score) {
        if (score >= 90) return "A'lo";
        if (score >= 80) return "Yaxshi";
        if (score >= 70) return "Qoniqarli";
        return "Qoniqarsiz";
    }

    private byte[] generateQRCode(CertificateData data) {
        // Генерация QR кода с информацией о сертификате
        String qrText = String.format("Certificate: %s, Name: %s, Course: %s, Score: %.1f",
                data.getListenerId(), data.getFullName(), data.getCourseName(), data.getScore());

        return Helper.generateQRCodeImage(qrText);
    }

    private ByteArrayOutputStream exportPDF(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }

}
