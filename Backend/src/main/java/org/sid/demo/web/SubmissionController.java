package org.sid.demo.web;


import lombok.AllArgsConstructor;
import org.sid.demo.DTO.SubmissionDto;
import org.sid.demo.Exception.DuplicateSubmissionException;
import org.sid.demo.Exception.SubmissionNotFoundException;
import org.sid.demo.services.SubmissionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @GetMapping("/submissions")
    public Iterable<SubmissionDto> getAllSubmissions(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return submissionService.getAllSubmissions(sortBy);
    }

    @GetMapping("/submissions/{id}")
    public SubmissionDto getSubmission(@PathVariable long id){
        return submissionService.getSubmission(id);
    }

    @GetMapping("/projects/{id}/submissions")
    public SubmissionDto getProjectSubmission(@PathVariable long id){
        return submissionService.getProjectSubmission(id);
    }

    @PostMapping("/projects/{id}/submissions")
    public ResponseEntity<?> registerSubmission(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            UriComponentsBuilder uriBuilder) throws IOException {


        SubmissionDto submissionDto = submissionService.registerSubmission(id, file);

        var uri = uriBuilder.path("/submissions/{id}").buildAndExpand(submissionDto.getId()).toUri();

        return ResponseEntity.created(uri).body(submissionDto);

    }

    @GetMapping("/projects/{id}/submissions/file")
    public ResponseEntity<Resource> downloadSubmissionFile(@PathVariable Long id) throws MalformedURLException, FileNotFoundException {
        Resource resource = submissionService.getSubmissionFile(id);
        String contentType = "application/pdf";
    return ResponseEntity.ok()
            .
    contentType(MediaType.parseMediaType(contentType))
            .
    header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\""+resource.getFilename() +"\"")
            .
    body(resource);
}

    @PutMapping("/projects/{id}/submissions")
    public SubmissionDto updateSubmission(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file ) throws IOException {

        return submissionService.updateSubmission(id,file);

    }

    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }



    @ExceptionHandler(DuplicateSubmissionException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateSubmission() {
        return ResponseEntity.badRequest().body(
                Map.of("submission", "a submission already exists for this project")
        );
    }

    @ExceptionHandler(SubmissionNotFoundException.class)
    public ResponseEntity<Void> handleSubmissionNotFound() {
        return ResponseEntity.notFound().build();
    }
}
