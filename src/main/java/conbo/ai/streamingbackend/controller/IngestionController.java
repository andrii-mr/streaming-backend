package conbo.ai.streamingbackend.controller;

import conbo.ai.streamingbackend.dto.DetectionsBatchRequest;
import conbo.ai.streamingbackend.service.IngestionService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/detections")
@AllArgsConstructor
public class IngestionController {

  @NonNull private final IngestionService ingestionService;

  @PostMapping("/batch")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void receiveDetections(@NonNull @RequestBody DetectionsBatchRequest request) {
    ingestionService.processBatch(request);
  }
}
