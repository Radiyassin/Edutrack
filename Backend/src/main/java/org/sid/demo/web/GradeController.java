package org.sid.demo.web;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.sid.demo.DTO.GradeDto;
import org.sid.demo.DTO.RegisterGradeRequest;
import org.sid.demo.DTO.UpdateGradeRequest;
import org.sid.demo.Exception.DuplicateGradeException;
import org.sid.demo.Exception.GradeNotFoundException;
import org.sid.demo.services.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
public class GradeController {
    private final GradeService gradeService;


    //delete grade
    //put grade
    @GetMapping("/grades")
    public Iterable<GradeDto> getAllGrades(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy){
        return gradeService.getAllGrades(sortBy);
    }

    @GetMapping("/grades/{id}")
    public GradeDto getGrade(@PathVariable Long id){
        return gradeService.getGrade(id);
    }

    @GetMapping("/submissions/{id}/grades")
    public GradeDto getSubmissionGrade(@PathVariable Long id){
        return gradeService.getSubmissionGrade(id);
    }

    @PostMapping("submissions/{id}/grades")
    public ResponseEntity<?> registerGrade (
            @PathVariable Long id,
            @Valid @RequestBody RegisterGradeRequest request,
            UriComponentsBuilder uriBuilder) {
        GradeDto gradeDto = gradeService.registerGrade(id, request);
        var uri = uriBuilder.path("/grades/{id}").buildAndExpand(gradeDto.getId()).toUri();
        return ResponseEntity.created(uri).body(gradeDto);
    }

    @PutMapping("submissions/{id}/grades")
    public GradeDto updateGrade(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UpdateGradeRequest request ) {

        return gradeService.updateGrade(id,request);

    }

    @DeleteMapping("/grades/{id}")
        public ResponseEntity<?> deleteGrade(@PathVariable Long id){

            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
        }




    @ExceptionHandler(GradeNotFoundException.class)
    public ResponseEntity<Void> handleGradeNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateGradeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateGrade() {
        return ResponseEntity.badRequest().body(
                Map.of("grade", "grade is already given.")
        );
    }
}
