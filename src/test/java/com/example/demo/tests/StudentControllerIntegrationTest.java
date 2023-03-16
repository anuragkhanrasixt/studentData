package com.example.demo.tests;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        studentRepository.deleteAll();
    }

    @AfterEach
    public void reset(){
        mockMvc = null;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoad(){
    }

    @Test
    public void testAddStudent() throws Exception {
        Student student = new Student("alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/register", student, Student.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<Student> students = studentRepository.findAll();
        System.out.println(students);
        assertThat(students, contains(hasProperty("email", is("alex@gmail.com"))));
    }


    @Test
    public void testGetStudentById() throws Exception{
        Student student = new Student("alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        studentRepository.save(student);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/id/"+String.valueOf(student.getId()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Student retrievedStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

        assertEquals(student.getEmail(), retrievedStudent.getEmail());
    }

    @Test
    public void testGetStudentByEmail() throws Exception{
        Student student = new Student("alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        studentRepository.save(student);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/email/"+student.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();


        Student retrievedStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        System.out.println(retrievedStudent);

        assertEquals(student.getEmail(), retrievedStudent.getEmail());
    }

    @Test
    public void testDeleteStudentById() throws Exception {
        Student student = new Student("alex", "alex@gmail.com", LocalDate.of(2000, 12, 10), "Male");
        studentRepository.save(student);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/"+String.valueOf(student.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}