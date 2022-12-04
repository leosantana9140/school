package br.com.school.controller;

import br.com.school.model.Grade;
import br.com.school.model.Student;
import br.com.school.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GradeController {
    private final StudentRepository studentRepository;

    public GradeController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/grade/{id}")
    public String getGradePage(@PathVariable String id, Model model) {
        Student student = this.studentRepository.findStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("grade", new Grade());

        return "student/grade";
    }

    @PostMapping("/grade/save/{id}")
    public String saveGrade(@PathVariable String id, @ModelAttribute Grade grade) {
        Student student = this.studentRepository.findStudentById(id);
        this.studentRepository.saveStudent(student.addGrade(student, grade));

        return "redirect:/student/list";
    }
}
