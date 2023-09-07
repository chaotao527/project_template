package com.singhand.sd.template.bizservice.controller;

import com.singhand.sd.template.bizmodel.model.response.OperationResponse;
import com.singhand.sd.template.bizservice.client.feign.BizBatchServiceClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "异步任务执行状态查询")
@RestController
@RequestMapping("operation")
public class OperationController {

  private final BizBatchServiceClient bizBatchServiceClient;

  @Autowired
  public OperationController(BizBatchServiceClient bizBatchServiceClient) {

    this.bizBatchServiceClient = bizBatchServiceClient;
  }

  @GetMapping("{id}")
  @PreAuthorize("isAuthenticated()")
  public OperationResponse getJobExecution(@PathVariable Long id) {

    return bizBatchServiceClient.getExecutionStatus(id);
  }

  @GetMapping("name/{name}/uuid/{uuid}")
  @PreAuthorize("isAuthenticated()")
  public OperationResponse getJobExecutionByNameAndUuid(@PathVariable String name,
      @PathVariable String uuid) {

    try {
      return bizBatchServiceClient.getExecutionStatusByJobNameAndUuid(
          name, uuid);
    } catch (Exception e) {
      if (name.equals("importWorkOrdersJob")) {
        final var operationResponse = new OperationResponse();
        operationResponse.setId(0);
        operationResponse.setName(name);
        operationResponse.setStatus("工单任务不存在");
        return operationResponse;
      }
      return bizBatchServiceClient.getExecutionStatusByJobNameAndUuid(
          name, uuid);
    }
  }

  @GetMapping("{name}/uuid/{uuid}")
  @PreAuthorize("isAuthenticated()")
  public OperationResponse getJobExecutionByJobNameAndUuid(@PathVariable String name,
      @PathVariable String uuid) {

    return bizBatchServiceClient.getExecutionStatusByJobNameAndUuid(name, uuid);
  }
}
