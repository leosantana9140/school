package br.com.school.controller;

import br.com.school.model.Student;
import br.com.school.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GeolocationController {
    private final StudentRepository studentRepository;

    public GeolocationController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/geolocation")
    public String getGeolocationPage(Model model) {
        List<Student> students = studentRepository.getAllStudents();
        model.addAttribute("students", students);

        return "student/geolocation";
    }

    @GetMapping("/geolocation/search")
    public String getGeolocationInformation(@RequestParam("studentId") String studentId, Model model) {
        Student student = studentRepository.findStudentById(studentId);
        List<Student> nearbyStudents = studentRepository.findByLocation(student);
        model.addAttribute("nearbyStudents", nearbyStudents);

        return "student/geolocation";
    }
}
