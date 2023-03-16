package com.example.demo.student;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {

        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());

        if (studentOptional.isPresent()) {
            throw new IllegalThreadStateException("email taken");
        }
        student.setAge();
        studentRepository.save(student);

    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException(studentId + " does not exist");
        }

        studentRepository.deleteById(studentId);
    }

    @KafkaListener(id = "name",topics = "update_student", groupId = "foo")
    public void updateName(String message) throws IOException {
        System.out.println("Listener: "+ message);
        Map<String, String> studentMap = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {});
        if (studentMap.containsKey("dob")){
            studentRepository.findAndUpdateDoB(LocalDate.parse(studentMap.get("dob")), Long.parseLong(studentMap.get("id")));
        }
        if (studentMap.containsKey("name")) {
            studentRepository.findAndUpdateName(studentMap.get("name"), Long.parseLong(studentMap.get("id")));
        }

    }


    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findStudentsByAge(age);
    }

    public List<Student> getGmail() {
        return studentRepository.findAllGmailUsers();
    }

    public Optional<Student> getStudentsID(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException(studentId + " does not exist");
        }
        return studentRepository.findById(studentId);
    }

    public Optional<Student> getStudentsByEmail(String email) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(email);

        if (studentOptional.isEmpty()) {
            throw new IllegalThreadStateException("email doesn't exist");
        }
        return studentOptional;
    }

    public List<Student> getStudentsByAandG(int age, String gender) {
        List<Student> studentOptional = studentRepository.findByAgeAndGender(age, gender);
        if (studentOptional.isEmpty()) {
            throw new IllegalStateException("Does not exist");
        }
        return studentOptional;
    }

}