package com.link8.tw.controller;


import com.link8.tw.controller.advice.ErrorCodeException;
import com.link8.tw.controller.request.point.PointDetailRequest;
import com.link8.tw.controller.request.point.PointListRequest;
import com.link8.tw.controller.response.point.PersonalPointResponse;
import com.link8.tw.controller.response.point.PointDetailResponse;
import com.link8.tw.controller.response.point.TaskStatisticResponse;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.service.ProjectService;
import com.link8.tw.service.TaskService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/point")
public class PointController {
    private final static Log LOG = LogFactory.getLog(PointController.class);

    @Autowired
    TaskService taskService;

    @GetMapping("/getPoint")
    public PersonalPointResponse getPersonalPoint() {
            return taskService.getPersonalPoint();
    }

    @GetMapping("/getPointList")
    public Page<TaskStatisticResponse> getPointList(@Valid PointListRequest request) throws ErrorCodeException {
        try {
            return taskService.getPointList(request);
        } catch (ProjectNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/getDetail")
    public Page<PointDetailResponse> getPointDetail(@Valid PointDetailRequest request) throws ErrorCodeException {
        try {
            return taskService.getPointDetail(request);
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

}
