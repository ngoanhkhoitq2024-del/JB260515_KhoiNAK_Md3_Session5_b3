package re.edu.md3ss5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import re.edu.md3ss5.dto.CourseResponse;
import re.edu.md3ss5.dto.CourseResponseV2;
import re.edu.md3ss5.dto.PageResponse;
import re.edu.md3ss5.entity.Course;
import re.edu.md3ss5.entity.CourseStatus;
import re.edu.md3ss5.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public PageResponse<CourseResponse> getPagedCourses(
            int page, int size, String sortBy, Sort.Direction direction) {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }
        if (!sortBy.equals("id") && !sortBy.equals("title") && !sortBy.equals("status")) {
            sortBy = "id";
        }

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Course> courses = courseRepository.findAll(pageable);

        Page<CourseResponse> courseResponses = courses.map(course -> new CourseResponse(
                course.getId(), course.getTitle(), course.getStatus()
        ));

        return new PageResponse<>(
                courseResponses.getContent(),
                courseResponses.getNumber(),
                courseResponses.getSize(),
                (int) courseResponses.getTotalElements(),
                courseResponses.getTotalPages(),
                courseResponses.isLast());
    }

    public PageResponse<CourseResponse> getPagedCoursesByStatus(
            int page, int size, String sortBy, Sort.Direction direction, CourseStatus status) {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }
        if (!sortBy.equals("id") && !sortBy.equals("title") && !sortBy.equals("status")) {
            sortBy = "id";
        }
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Course> courses = courseRepository.findAllByStatus(status, pageable);

        Page<CourseResponse> courseResponses = courses.map(
                course -> new CourseResponse(
                        course.getId(), course.getTitle(), course.getStatus()
                ));

        return new PageResponse<>(
                courseResponses.getContent(),
                courseResponses.getNumber(),
                courseResponses.getSize(),
                (int) courseResponses.getTotalElements(),
                courseResponses.getTotalPages(),
                courseResponses.isLast()
        );
    }
}

/*
  Là Course có thể chứa thông tin nhạy cảm nên tạo ra CouseResponse để chỉ in ra những thông tin
   muốn in nên khi lấy từ db là nó đang là Course nên đổi sang CourseResponse,
   còn return new PageResponse<> là vì Page<CourseResponse> chứa nhiều thông tin của Page
   nên tạo ra class PageResponse để chỉ trả về những thông tin cần

 - Page<CourseResponse> là để đổi Course -> CourseResponse
  -> Mục đích của CourseResponse là chỉ chứa những field muốn trả ra API, tránh trả trực tiếp Entity
 - PageResponse là đổi Page -> PageResponse
  -> PageResponse lấy những thông tin cần thiết từ Page rồi đóng gói thành một format response
 */