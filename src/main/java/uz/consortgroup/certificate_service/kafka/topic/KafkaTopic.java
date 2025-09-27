package uz.consortgroup.certificate_service.kafka.topic;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaTopic {
    @Value("${kafka.certificate-issuance}")
    private String certificateIssuance;

}
