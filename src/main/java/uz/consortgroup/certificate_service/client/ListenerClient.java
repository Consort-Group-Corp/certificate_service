package uz.consortgroup.certificate_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.consortgroup.core.api.v1.dto.user.response.UserShortInfoResponseDto;

import java.util.UUID;

@FeignClient(name = "user-service", contextId = "userClient", url = "${user.service.url}", configuration = FeignClientConfig.class)
public interface ListenerClient {

    @GetMapping("/api/v1/internal/users/{id}/short-info")
    UserShortInfoResponseDto getRawShortInfo(@PathVariable("id") UUID id);
}
