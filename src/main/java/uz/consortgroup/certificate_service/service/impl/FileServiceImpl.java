package uz.consortgroup.certificate_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;
import uz.consortgroup.certificate_service.dto.CertificateDto;
import uz.consortgroup.certificate_service.entity.Certificate;
import uz.consortgroup.certificate_service.exception.CertificateNotFoundException;
import uz.consortgroup.certificate_service.exception.FileGeneratingException;
import uz.consortgroup.certificate_service.service.FileService;

import java.io.*;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${cert-directory}")
    private String fileDirectory;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public boolean generateCertificate(CertificateDto certificateDto, UUID certId) {
        try {
            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(Collections.singletonList(certificateDto));

            // Фон сертификата
            URL BACKGROUND_IMAGE_URL = this.getClass()
                    .getClassLoader()
                    .getResource("image/certificate(" + certificateDto.getCertificateTemplate().name() + ").jpg");

            if (BACKGROUND_IMAGE_URL == null || certificateDto.getCertificateTemplate().equals(CertificateTemplate.UNKNOWN)) {
                throw new FileNotFoundException("Файл фона certificate.jpg не найден в resources/image/");
            }

            // Параметры для Jasper
            Map<String, Object> params = new HashMap<>();
            params.put("BACKGROUND_IMAGE", BACKGROUND_IMAGE_URL.toString());
            params.put("CERTIFICATE_NUMBER", certificateDto.getSerialNumber());
            params.put("RECIPIENT_NAME", certificateDto.getListenerFullName());
            params.put("COURSE_NAME", certificateDto.getCourseName());
            params.put("ISSUE_DATE", certificateDto.getIssuedDate());

            // Компиляция JRXML (certificate.jrxml должен лежать в reportsDir)
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    this.getClass().getResourceAsStream("/templates/certificate.jrxml")
            );

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Экспорт в PDF
            ByteArrayOutputStream byteArrayOutputStream = exportPDF(jasperPrint);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            // Сохранение PDF
            saveToFile(bytes, certificateDto.getUploadPath());

            return true;

        } catch (Exception e) {
            log.error("Ошибка при генерации сертификата", e);
            throw new FileGeneratingException("Ошибка при генерации сертификата");
        }
    }

    @Override
    public FileSystemResource download(Certificate certificate) {
        String uploadPath = fileDirectory + certificate.getUploadPath();
        File file = new File(uploadPath);

        if (!file.exists()) {
            throw new CertificateNotFoundException("File not found: " + uploadPath);
        }

        return new FileSystemResource(file);
    }

    private ByteArrayOutputStream exportPDF(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }

    private void saveToFile(byte[] bytes, String fileName) throws IOException {
        File dir = new File(fileDirectory);
        if (!dir.exists() && !dir.mkdirs()) {
            log.warn("Не удалось создать директорию: {}", dir.getAbsolutePath());
        }
        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }
}
