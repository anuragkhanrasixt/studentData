package com.example.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.email like '%@gmail.com'")
    List<Student> findAllGmailUsers();

    @Query("SELECT s FROM Student s WHERE s.age >= ?#{[0]}")
    List<Student> findStudentsByAge(int age);

    @Query("SELECT s FROM Student s WHERE s.age = :age and s.gender = :gender")
    List<Student> findByAgeAndGender(@Param("age") int age, @Param("gender") String gender);

    @Modifying @Transactional
    @Query("UPDATE Student s SET s.name = :name WHERE s.id = :id")
    Void findAndUpdateName(@Param("name") String Name, @Param("id") Long id);

    @Modifying @Transactional
    @Query("UPDATE Student s SET s.dob = :dob WHERE s.id = :id")
    Void findAndUpdateDoB(@Param("dob") LocalDate dob, @Param("id") Long id);


}