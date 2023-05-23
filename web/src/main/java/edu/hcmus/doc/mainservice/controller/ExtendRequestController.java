package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.ExtendRequestDto;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.service.ExtendRequestService;
import edu.hcmus.doc.mainservice.util.mapper.ExtendRequestMapper;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/extend-requests")
public class ExtendRequestController {

  private final ExtendRequestService extendRequestService;

  private final ExtendRequestMapper extendRequestMapper;

  @GetMapping("/{username}")
  public List<ExtendRequestDto> getCurrUserExtensionRequests(@PathVariable String username) {
    return extendRequestService.getExtensionRequestsByUsername(username)
        .stream()
        .map(extendRequestMapper::toDto)
        .toList();
  }

  @GetMapping("/can-validate/{extendRequestId}")
  public boolean canValidateExtendRequest(@PathVariable Long extendRequestId) {
    return extendRequestService.canCurrentUserValidateExtendRequest(extendRequestId);
  }

  @PostMapping
  public Long createExtensionRequest(@RequestBody @Valid ExtendRequestDto extendRequestDto) {
    return extendRequestService.createExtensionRequest(
        extendRequestDto.getProcessingUserId(),
        extendRequestMapper.toEntity(extendRequestDto));
  }

  @PutMapping("/{id}/{status}")
  public Long validateExtensionRequest(
      @PathVariable Long id,
      @PathVariable ExtendRequestStatus status) {
    return extendRequestService.validateExtensionRequest(id, status);
  }
}
