package br.com.school.controller;

import br.com.school.model.Student;
import br.com.school.repository.StudentRepository;
import br.com.school.service.GeolocationService;
import com.google.maps.errors.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {
    private final StudentRepository studentRepository;
    private final GeolocationService geolocationService;

    public StudentController(StudentRepository studentRepository, GeolocationService geolocationService) {
        this.studentRepository = studentRepository;
        this.geolocationService = geolocationService;
    }

    @GetMapping("/student/list")
    public String getListPage(Model model) {
        List<Student> students = this.studentRepository.getAllStudents();
        model.addAttribute("students", students);

        return "student/list";
    }

    @GetMapping("/student/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("student", new Student());
        return "student/register";
    }

    @GetMapping("/student/edit/{id}")
    public String getEditPage(@PathVariable String id, Model model) {
        Student student = this.studentRepository.findStudentById(id);
        model.addAttribute("student", student);

        return "student/edit";
    }

    @GetMapping("/student/find")
    public String findStudentByAverage(@RequestParam("status") String status, Model model) {
        List<Student> students = this.studentRepository.findByGrade(status);
        model.addAttribute("students", students);

        return "student/list";
    }

    @PostMapping("/student/save")
    public String saveStudent(@ModelAttribute Student student) throws NotFoundException {
        try {
            List<Double> latitudeLongitude = geolocationService.getLatitudeAndLongitude(student.getAddress().getStreet());
            student.getAddress().setCoordinates(latitudeLongitude);
            studentRepository.saveStudent(student);
        } catch (Exception exception) {
            throw new NotFoundException("Address not found!");
        }

        return "redirect:/student/list";
    }

    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        this.studentRepository.deleteUser(id);

        return "redirect:/student/list";
    }
}
