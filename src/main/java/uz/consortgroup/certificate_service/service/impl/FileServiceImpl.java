package uz.consortgroup.certificate_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.dto.CertificateData;
import uz.consortgroup.certificate_service.dto.CreateCertificateReqDto;
import uz.consortgroup.certificate_service.service.FileService;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Value("${jasper.report-template-directories.reportsDir}")
    private String reportsDir;

    @Override
    public byte[] generateCertificate(CreateCertificateReqDto certificateRequest) {
        try {
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            // Данные слушателя и курса
            CertificateData certificateData = getCertificateData(
                    certificateRequest.getListenerId(),
                    certificateRequest.getCourseId(),
                    certificateRequest.getScore()
            );

            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(Collections.singletonList(certificateData));

            // Фон сертификата (certificate(BLUE).jpg
            URL BACKGROUND_IMAGE_URL = this.getClass()
                    .getClassLoader()
                    .getResource("image/certificate(BLUE).jpg");

            if (BACKGROUND_IMAGE_URL == null) {
                throw new FileNotFoundException("Файл фона certificate(BLUE).jpg не найден в resources/image/");
            }

            // Параметры для Jasper
            String certificateNumber = generateCertificateNumber();
            Map<String, Object> params = new HashMap<>();
            params.put("BACKGROUND_IMAGE", BACKGROUND_IMAGE_URL.toString());
            params.put("CERTIFICATE_NUMBER", certificateNumber);
            params.put("RECIPIENT_NAME", certificateData.getFullName());
            params.put("COURSE_NAME", certificateData.getCourseName());
            params.put("ISSUE_DATE", format);

            // Компиляция JRXML (certificate.jrxml должен лежать в reportsDir)
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    resourceLoader.getResource(reportsDir + File.separator + "certificate.jrxml").getInputStream()
            );

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Экспорт в PDF
            ByteArrayOutputStream byteArrayOutputStream = exportPDF(jasperPrint);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            // Сохранение PDF
            saveToFile(bytes, certificateNumber + ".pdf");

            return bytes;

        } catch (Exception e) {
            log.error("Ошибка при генерации сертификата", e);
            throw new RuntimeException("Ошибка при генерации сертификата", e);
        }
    }

    private CertificateData getCertificateData(String listenerId, String courseId, Double score) {
        CertificateData data = new CertificateData();
        data.setFullName("F.I.Sharipov"); // заглушка, потом подтянешь из БД
        data.setCourseName("Korrupsiyaga qarshi kurash bo'yicha malaka oshirish kursi");
        data.setListenerId(listenerId);
        data.setCourseId(courseId);
        data.setScore(score);
        return data;
    }

    private String generateCertificateNumber() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private ByteArrayOutputStream exportPDF(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }

    private void saveToFile(byte[] bytes, String fileName) throws IOException {
        File dir = new File("src/main/resources/certificates");
        if (!dir.exists() && !dir.mkdirs()) {
            log.warn("Не удалось создать директорию: {}", dir.getAbsolutePath());
        }
        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
            log.info("✅ Сертификат сохранён: {}", file.getAbsolutePath());
        }
    }
}
