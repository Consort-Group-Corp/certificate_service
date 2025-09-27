package uz.consortgroup.certificate_service.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCertificateReqDto {
    @NotNull(message = "Listener id is required")
    @JsonProperty("listener_id")
    private UUID listenerId;

    @NotNull(message = "Course id is required")
    @JsonProperty("course_id")
    private UUID courseId;

    @NotNull(message = "Score id is required")
    @JsonProperty("score")
    private Double score;
}
