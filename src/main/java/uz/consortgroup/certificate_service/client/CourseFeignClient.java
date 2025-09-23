package uz.consortgroup.certificate_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.consortgroup.core.api.v1.dto.course.response.course.CourseResponseDto;

import java.util.UUID;

@FeignClient(
        name = "course-service",
        contextId = "courseClient",
        url = "${course.service.url}",
        configuration = FeignClientConfig.class)
public interface CourseFeignClient {
    @GetMapping("/api/v1/courses/{courseId}")
    CourseResponseDto getCourseById(@PathVariable("courseId") UUID courseId);
}
