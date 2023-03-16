package com.example.demo.tests;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StudentRepositoryUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddStudent(){
        Student student = new Student(1L, "alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentRepository.save(student));
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student(1L, "alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentRepository.findById(1L);
        assertEquals(student, result.get());
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student(1L, "alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentRepository.save(student));
        student.setEmail("alex@yahoo.com");
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentRepository.save(student));
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student(1L, "alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        doNothing().when(studentRepository).delete(student);
        studentRepository.delete(student);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    public void testGetAllStudents() {
        List<Student> students = new ArrayList<>();
        Student alex = new Student(1L, "alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        Student anurag = new Student(1L, "anurag", "anurag@gmail.com", LocalDate.of(2001, 10, 12), "Male");
        when(studentRepository.findAll()).thenReturn(students);
        List<Student> result = studentRepository.findAll();
        assertEquals(students.size(), result.size());
    }
}
