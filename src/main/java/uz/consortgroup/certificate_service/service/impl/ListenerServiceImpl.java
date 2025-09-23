package uz.consortgroup.certificate_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.client.ListenerClient;
import uz.consortgroup.certificate_service.exception.ListenerNotFoundException;
import uz.consortgroup.certificate_service.service.ListenerService;
import uz.consortgroup.core.api.v1.dto.user.response.UserShortInfoResponseDto;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListenerServiceImpl implements ListenerService {
    private final ListenerClient listenerClient;

    @Override
    public UserShortInfoResponseDto getListenerShortInfo(UUID userId) {
        log.info("Getting Listener info for userId={}", userId);

        UserShortInfoResponseDto raw = listenerClient.getRawShortInfo(userId);
        if (Objects.isNull(raw))
            throw new ListenerNotFoundException("Listener not found by listener_id=" + userId.toString());

        UserShortInfoResponseDto result = UserShortInfoResponseDto.builder()
                .id(raw.getId())
                .firstName(raw.getFirstName())
                .lastName(raw.getLastName())
                .middleName(raw.getMiddleName())
                .position(raw.getPosition())
                .build();

        log.debug("Listener info: {}", result);
        return result;
    }
}
