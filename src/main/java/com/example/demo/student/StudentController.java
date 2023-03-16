package com.example.demo.student;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    private static final String TOPIC = "update_student";
    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(path = "/api/v1/student")
    public List<Student> getStudents(){
        return studentService.getStudents();
    }

    @GetMapping(path = "/api/v1/gmail")
    public List<Student> getGmail(){
        return studentService.getGmail();
    }

    @GetMapping(path = "/api/v1/age/{age}")
    public List<Student> getOldStudents(@PathVariable("age") int age){
        return studentService.getStudentsByAge(age);
    }

    @GetMapping(path = "/api/v1/ageGender/{age}/{gender}")
    public List<Student> getStudentsByAgeAndGender(@PathVariable("age") int age, @PathVariable("gender") String gender){
        return studentService.getStudentsByAandG(age, gender);
    }

    @GetMapping(path = "/api/v1/student/id/{studentID}")
    public Optional<Student> getStudentsByID(@PathVariable("studentID") Long studentID){
        return studentService.getStudentsID(studentID);
    }

    @PostMapping(path = "/api/v1/register")
    public void register(@RequestBody Student student){
        studentService.addNewStudent(student);
    }

    @PostMapping(path = "/api/v1/update/{studentID}")
    public void updateStudent(@RequestBody HashMap<String, String> studentMap, @PathVariable("studentID") String studentID) throws JsonProcessingException {

        studentMap.put("id", studentID);
        String message = objectMapper.writeValueAsString(studentMap);
        System.out.println("Producer: "+message);
        kafkaTemplate.send(TOPIC, message);
    }


    @GetMapping(path = "/api/v1/student/email/{email}")
    public Optional<Student> getStudentsByID(@PathVariable("email") String email){
        return studentService.getStudentsByEmail(email);
    }

    @DeleteMapping(path = "/api/v1/delete/{studentID}")
    public void deleteStudent(@PathVariable("studentID") Long studentId){
        studentService.deleteStudent(studentId);
    }
}