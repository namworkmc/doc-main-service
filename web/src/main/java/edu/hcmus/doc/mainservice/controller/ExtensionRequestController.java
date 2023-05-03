package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.ExtensionRequestDto;
import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
import edu.hcmus.doc.mainservice.service.ExtensionRequestService;
import edu.hcmus.doc.mainservice.util.mapper.ExtensionRequestMapper;
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
@RequestMapping(DocURL.API_V1 + "/extension-requests")
public class ExtensionRequestController {

  private final ExtensionRequestService extensionRequestService;

  private final ExtensionRequestMapper extensionRequestMapper;

  @GetMapping("/{username}")
  public List<ExtensionRequestDto> getCurrUserExtensionRequests(@PathVariable String username) {
    return extensionRequestService.getExtensionRequestsByUsername(username)
        .stream()
        .map(extensionRequestMapper::toDto)
        .toList();
  }

  @PostMapping
  public Long createExtensionRequest(@RequestBody @Valid ExtensionRequestDto extensionRequestDto) {
    return extensionRequestService.createExtensionRequest(
        extensionRequestDto.getProcessingUserId(),
        extensionRequestMapper.toEntity(extensionRequestDto));
  }

  @PutMapping("/{id}/{validatorId}/{status}")
  public Long validateExtensionRequest(
      @PathVariable Long id,
      @PathVariable Long validatorId,
      @PathVariable ExtensionRequestStatus status) {
    return extensionRequestService.validateExtensionRequest(id, validatorId, status);
  }
}
