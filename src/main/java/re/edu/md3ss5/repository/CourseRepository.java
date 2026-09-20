package re.edu.md3ss5.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import re.edu.md3ss5.dto.CourseResponseV2;
import re.edu.md3ss5.entity.Course;
import re.edu.md3ss5.entity.CourseStatus;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.status = :status")
    Page<Course> findAllByStatus(
            @Param("status") CourseStatus status,
            Pageable pageable);
}
