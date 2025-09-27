package uz.consortgroup.certificate_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.consortgroup.certificate_service.client.CourseFeignClient;
import uz.consortgroup.certificate_service.exception.CourseNotFoundException;
import uz.consortgroup.certificate_service.exception.ListenerNotFoundException;
import uz.consortgroup.certificate_service.service.CourseService;
import uz.consortgroup.core.api.v1.dto.course.response.course.CourseResponseDto;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseFeignClient courseFeignClient;

    @Override
    public CourseResponseDto getCourseTitleById(UUID courseId) {
        log.info("Getting Course info for courseId={}", courseId);

        CourseResponseDto raw = courseFeignClient.getCourseById(courseId);
        if (Objects.isNull(raw))
            throw new CourseNotFoundException("Course not found by course_id=" + courseId.toString());

        CourseResponseDto result = CourseResponseDto.builder()
                .id(raw.getId())
                .courseStatus(raw.getCourseStatus())
                .courseType(raw.getCourseType())
                .translations(raw.getTranslations())
                .build();

        log.debug("Course info: {}", result);
        return result;
    }
}
