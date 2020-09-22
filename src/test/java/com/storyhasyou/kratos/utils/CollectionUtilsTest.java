package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author fangxi created by 2020/8/31
 */
public class CollectionUtilsTest {

    @Test
    public void map() {
        List<Person> people = Lists.newArrayList(new Person(11, "a"), new Person(12, "b"), new Person(13, "c1"));
        List<Student> students = Lists.newArrayList(new Student(1, "a"), new Student(2, "b"), new Student(3, "c"));
        List<StudentDTO> studentDTOList = CollectionUtils.marge(people, students, Person::getName, Student::getName, (person, student) -> {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setAge(person.getAge());
            studentDTO.setGrade(student.getGrade());
            studentDTO.setName(person.getName());
            return studentDTO;
        });

        Console.log(studentDTOList);

    }

    @Test
    public void find() {
        List<Person> people = Lists.newArrayList(new Person(11, "a"), new Person(12, "b"), new Person(13, "c1"));
        Person one = CollectionUtils.find(people, person -> person.getAge() == 111);
        Console.log(one);
    }


    @Data
    @AllArgsConstructor
    public static class Person {
        private int age;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Student {
        private int grade;
        private String name;
    }

    @Data
    public static class StudentDTO {
        private int age;
        private int grade;
        private String name;
    }


}
