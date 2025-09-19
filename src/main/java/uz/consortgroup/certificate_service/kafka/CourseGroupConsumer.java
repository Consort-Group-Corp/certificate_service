package uz.consortgroup.certificate_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class CourseGroupConsumer extends AbstractKafkaConsumer<CourseGroupOpenedEvent> {
    private final CourseGroupOpenedEventProcessor courseGroupOpenedEventProcessor;

    public CourseGroupConsumer(CourseGroupOpenedEventProcessor courseGroupOpenedEventProcessor) {
        this.courseGroupOpenedEventProcessor = courseGroupOpenedEventProcessor;
    }

    @KafkaListener(
            topics = "${kafka.course-group}",
            groupId = "${kafka.consumer-group-id}",
            containerFactory = "universalKafkaListenerContainerFactory"
    )
    public void onMessage(List<CourseGroupOpenedEvent> events, Acknowledgment ack) {
        log.info("Received {} user-created events", events.size());
        processBatch(events, ack);
    }

    @Override
    protected void handleMessage(CourseGroupOpenedEvent events) {
        courseGroupOpenedEventProcessor.process(List.of(events));
    }


    @Override
    protected UUID getMessageId(CourseGroupOpenedEvent event) {
        return event.getMessageId();
    }
}
