package br.com.school.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private ObjectId id;
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
    private List<Grade> grade;
    private Double average = 0.0;
    private Address address;

    public Student() { }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Grade> getGrade() {
        if (this.grade == null) {
            this.grade = new ArrayList<>();
        }
        return grade;
    }

    public void setGrade(List<Grade> grade) {
        this.grade = grade;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Student generateId() {
        setId(new ObjectId());
        return this;
    }

    public Student addGrade(Student student, Grade grade) {
        List<Grade> gradeList = student.getGrade();
        gradeList.add(grade);
        student.setGrade(gradeList);
        return student;
    }
}
