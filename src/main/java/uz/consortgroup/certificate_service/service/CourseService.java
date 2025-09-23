package uz.consortgroup.certificate_service.service;

import uz.consortgroup.core.api.v1.dto.course.response.course.CourseResponseDto;
import uz.consortgroup.core.api.v1.dto.user.response.UserShortInfoResponseDto;

import java.util.UUID;

public interface CourseService {
    CourseResponseDto getCourseTitleById(UUID courseId);
}
