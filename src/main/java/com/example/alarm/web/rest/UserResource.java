package com.example.alarm.web.rest;

import com.example.alarm.domain.User;
import com.example.alarm.repository.NoticeRepository;
import com.example.alarm.repository.UserRepository;
import com.example.alarm.service.NoticeService;
import com.example.alarm.web.rest.dto.NoticeDTO;
import com.example.alarm.web.rest.dto.UserDTO;
import com.example.alarm.web.rest.errors.BadRequestAlertException;
import com.example.alarm.web.rest.mapper.UserMapper;
import com.netflix.discovery.converters.Auto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.alarm.domain.Notice}.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private static final String ENTITY_NAME = "alarmUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResource(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNotice(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new notice cannot already have an ID", ENTITY_NAME, "idexists");
        }

        User result = userRepository.save(userMapper.toEntity(userDTO));
        return ResponseEntity
            .created(new URI("/api/notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
