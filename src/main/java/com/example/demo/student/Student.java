package com.example.demo.student;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;


@Entity
@Table(name = "student")
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )

    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private Integer age;
    private String gender;


    public Student() {
    }

    public Student(String name, String email, LocalDate dob, String gender) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }

    public Student(Long id, String name, String email, LocalDate dob, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge() {
        this.age = Period.between(this.dob, LocalDate.now()).getYears();
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
