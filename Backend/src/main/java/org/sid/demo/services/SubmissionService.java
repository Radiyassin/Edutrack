package org.sid.demo.services;


import lombok.AllArgsConstructor;
import org.sid.demo.DTO.SubmissionDto;
import org.sid.demo.Exception.DuplicateSubmissionException;
import org.sid.demo.Exception.ProjectNotFoundException;
import org.sid.demo.Exception.SubmissionNotFoundException;
import org.sid.demo.entities.Submission;
import org.sid.demo.mappers.SubmissionMapper;
import org.sid.demo.repositories.GradeRepository;
import org.sid.demo.repositories.ProjectRepository;
import org.sid.demo.repositories.SubmissionRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SubmissionService {
    private final ProjectRepository projectRepository;

    private final SubmissionRepository submissionRepository;

    private final GradeRepository gradeRepository;

    private final SubmissionMapper submissionMapper;

    private final String uploadDir = Paths.get("uploads").toAbsolutePath().toString();

    public SubmissionDto registerSubmission(long projectId, MultipartFile file) throws IOException {

        var project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (submissionRepository.existsByProjectId(projectId)) {
            throw new DuplicateSubmissionException();
        }

        String uniqueFileName = saveUploadedFile(file);

        Submission submission = new Submission();
        submission.setFileUrl(uniqueFileName);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setProject(project);

        submissionRepository.save(submission);

        return submissionMapper.toDto(submission);

    }

    public SubmissionDto updateSubmission(long projectId, MultipartFile file) throws IOException {
        var project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        var submission = submissionRepository.findByProjectId(projectId).orElseThrow(SubmissionNotFoundException::new);
        String oldFilePath = uploadDir + File.separator + submission.getFileUrl();

        File oldFile = new File(oldFilePath);
        if (oldFile.exists()) {
            oldFile.delete();
        }
        String uniqueFileName = saveUploadedFile(file);


        submission.setFileUrl(uniqueFileName);
        submission.setSubmittedAt(LocalDateTime.now());

        submissionRepository.save(submission);

        return submissionMapper.toDto(submission);


    }

    public Resource getSubmissionFile(Long id) throws MalformedURLException, FileNotFoundException {

        var project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);


        Submission submission = submissionRepository.findByProjectId(id).orElseThrow(SubmissionNotFoundException::new);

        String fileName = submission.getFileUrl();//chof chno f had fileUrl smia

        Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);

        Resource resource = new UrlResource(filePath.toUri());

        if(resource.exists() && resource.isReadable()){
            return resource;
        } else {
            throw new FileNotFoundException("file not found or not readable");
        }

    }


    public SubmissionDto getProjectSubmission(Long id){
        var project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        Submission submission = submissionRepository.findByProjectId(id).orElseThrow(SubmissionNotFoundException::new);

        return submissionMapper.toDto(submission);

    }

    public Iterable<SubmissionDto> getAllSubmissions(String sortBy) {
        if (!Set.of("submittedAt" , "id").contains(sortBy)) {
            sortBy = "submittedAt";
        }

        var submissions = submissionRepository.findAll(Sort.by(sortBy));
        return submissions.stream()
                .map(submissionMapper::toDto)
                .toList();
    }

    public SubmissionDto getSubmission(Long id){
        var submission = submissionRepository.findById(id).orElseThrow(SubmissionNotFoundException::new);


        return submissionMapper.toDto(submission);
    }

    public void deleteSubmission (Long id){
        var submission = submissionRepository.findById(id).orElseThrow(SubmissionNotFoundException::new);
        gradeRepository.deleteBySubmissionId(submission.getId());
        submissionRepository.deleteById(id);
    }


    private String saveUploadedFile(MultipartFile file) throws IOException {

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();


        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;


        String filePath = uploadDir + File.separator + uniqueFileName;
        file.transferTo(new File(filePath));

        return uniqueFileName;
    }


}
