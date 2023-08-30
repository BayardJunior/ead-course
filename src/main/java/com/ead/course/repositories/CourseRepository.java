package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {
    @Query(value = "select case when count(tcu) > 0 then true else false end from TB_COURSE_USERS tcu where tcu.course_id =:courseId and tcu.user_id =:userId", nativeQuery = true)
    boolean existsByCourseAndUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "insert into TB_COURSE_USERS values (:courseId, :userId)", nativeQuery = true)
    void saveSubscriptionUserinCourse(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "delete from TB_COURSE_USERS where course_id =:courseId", nativeQuery = true)
    void deleteCourseUserByCourseId(@Param("courseId") UUID courseId);

    @Modifying
    @Query(value = "delete from TB_COURSE_USERS where user_id =:userId", nativeQuery = true)
    void deleteCourseUserByUserId(@Param("userId") UUID userId);
}
