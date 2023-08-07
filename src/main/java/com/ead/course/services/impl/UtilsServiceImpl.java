package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    public String getUrlToAllUsersByCourseId(UUID courseId, Pageable pageable) {
        return "/users?courseId="
                .concat(courseId.toString())
                .concat("&page=".concat(String.valueOf(pageable.getPageNumber())))
                .concat("&size=".concat(String.valueOf(pageable.getPageSize())))
                .concat("&sort=").concat(pageable.getSort().toString().replaceAll(": ", ","));
    }
}
