package uz.consortgroup.certificate_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.consortgroup.core.api.v1.dto.user.request.NotificationCreateRequestDto;

@FeignClient(name = "notification-service", url = "${notification.service.url}", configuration = FeignClientConfig.class)
public interface NotificationClient {

    @PostMapping("/api/v1/notifications")
    void createNotification(@RequestBody NotificationCreateRequestDto request);
}
