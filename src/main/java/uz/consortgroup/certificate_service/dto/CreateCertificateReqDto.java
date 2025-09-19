package uz.consortgroup.certificate_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCertificateReqDto {
    @NotNull(message = "Listener id is required")
    @JsonProperty("listener_id")
    private String listenerId;

    @NotNull(message = "Course id is required")
    @JsonProperty("course_id")
    private String courseId;

    @NotNull(message = "Score id is required")
    @JsonProperty("score")
    private Double score;

    @NotNull(message = "Certificate Template is required")
    @JsonProperty("certificate_template")
    private CertificateTemplate certificateTemplate;
}
