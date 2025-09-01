package org.sid.demo.services;


import lombok.AllArgsConstructor;
import org.sid.demo.DTO.ProjectDto;
import org.sid.demo.DTO.RegisterProjectRequest;
import org.sid.demo.DTO.UpdateProjectRequest;
import org.sid.demo.DTO.UserDto;
import org.sid.demo.Exception.*;
import org.sid.demo.entities.Project;
import org.sid.demo.entities.Role;
import org.sid.demo.entities.User;
import org.sid.demo.mappers.ProjectMapper;
import org.sid.demo.mappers.UserMapper;
import org.sid.demo.repositories.GradeRepository;
import org.sid.demo.repositories.ProjectRepository;
import org.sid.demo.repositories.SubmissionRepository;
import org.sid.demo.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@AllArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeRepository gradeRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;

    public Iterable<ProjectDto> getAllProjects(String sortBy){
        if (!Set.of("title", "createdAt").contains(sortBy))
            sortBy = "title";

        return projectRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public ProjectDto getProject(long id){
        var project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        return projectMapper.toDto(project);

    }

    public Iterable<UserDto> getProjectMembers(long id , String sortBy){
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";

        var project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
     // var members = userRepository.findByProjectId(id, Sort.by(sortBy)); to get all users including teachers
        var members = userRepository.findByProjectIdAndRole(id, Role.STUDENT, Sort.by(sortBy));
        return members.stream()
                .map(userMapper::toDto)
                .toList();

    }

    public ProjectDto registerProject (RegisterProjectRequest request, String studentEmail){
        System.out.println("=== DEBUT registerProject ===");
        System.out.println("Email étudiant: " + studentEmail);
        System.out.println("Titre projet: " + request.getTitle());
        System.out.println("Description: " + request.getDescription());

        if (projectRepository.existsByTitle(request.getTitle())) {
            throw new DuplicateProjectException();
        }

        User student = userRepository.findByEmail(studentEmail);
        var project = projectMapper.toEntity(request);
        project.addMember(student);
        projectRepository.save(project);

        System.out.println("Projet créé avec succès, ID: " + project.getId());
        return projectMapper.toDto(project);
    }

    public ProjectDto updateProject (Long id , UpdateProjectRequest request) {
        var project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        projectMapper.update(request,project);
        projectRepository.save(project);
        return projectMapper.toDto(project);

    }

    @Transactional
    public void deleteProject(Long id) {

        var project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        var submission = submissionRepository.findByProjectId(id).orElseThrow(SubmissionNotFoundException::new);

        gradeRepository.deleteBySubmissionId(submission.getId());

        submissionRepository.deleteById(submission.getId());

        projectRepository.deleteById(id);
    }

    public ProjectDto addMemberToProject(Long projectId, String userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (project.getMembers().contains(user)) {
            throw new MemberAlreadyExistsException();
        }
        if(user.getProject()!=null){
            throw new UserAlreadyInAProjectException();
        }

        project.addMember(user);
        projectRepository.save(project);

        return projectMapper.toDto(project);
    }
    @Transactional
    public void removeMemberFromProject(Long projectId, String userId){
        Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!project.getMembers().contains(user)) {
            throw new MemberNotFoundException();
        }
        project.removeMember(user);
        if (project.getMembers().isEmpty()) {
            projectRepository.delete(project);
        }

    }


}
