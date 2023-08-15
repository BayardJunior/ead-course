package com.ead.course.infrastructure.components;

import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.infrastructure.dto.SubscriptionUserInCourseDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Log4j2
@Component
public class AuthUserComponentImpl {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${ead.api.url.auth-user}")
    private String DEFAULT_URI_AUTH_USER_SERVICES;

    public Page<UserDto> findAllUsersByCourse(UUID courseId, Pageable pageable) {

        ResponseEntity<ResponsePageDto<UserDto>> result = null;
        String url = DEFAULT_URI_AUTH_USER_SERVICES.concat(this.utilsService.getUrlToAllUsersByCourseId(courseId, pageable));

        log.debug("Request Url: {}", url);
        log.info("Request Url: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {
            };
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            log.debug("Response Number of Elements Url: {}", result.getBody().getContent().size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /users: {}", e);
        }
        log.info("Ending request /users userId {} ", courseId);

        return result.getBody();
    }

    public ResponseEntity<UserDto> findUsersById(UUID userId) {

        String url = DEFAULT_URI_AUTH_USER_SERVICES.concat(this.utilsService.getUrlToFindUserById(userId));

        log.debug("Request Url: {}", url);
        log.info("Request Url: {}", url);

        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
    }

    public void sendSubscpritionToAuthUser(UUID courseId, UUID userId) {

        String url = DEFAULT_URI_AUTH_USER_SERVICES.concat(this.utilsService.getUrlToSaveSubscriptionIntoCourse(userId));
        SubscriptionUserInCourseDto dto = new SubscriptionUserInCourseDto();
        dto.setUserId(userId);
        dto.setCourseId(courseId);

        restTemplate.postForObject(url, dto, String.class);

    }

    public void deleteCourseInAuthUser(UUID courseId) {

        String url = DEFAULT_URI_AUTH_USER_SERVICES.concat(this.utilsService.deleteUrlUserCourseByCourse(courseId));

        log.debug("Request Url: {}", url);
        log.info("Request Url: {}", url);

        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
