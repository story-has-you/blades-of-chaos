package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/8/31
 */
public class CollectionUtilsTest {

    @Test
    public void map() {
        List<Person> people = Lists.newArrayList(
                new Person(11, "a"),
                new Person(12, "b"),
                new Person(13, "c")
        );
        List<Student> students = Lists.newArrayList(
                new Student(1, "a"),
                new Student(2, "b"),
                new Student(3, "c"),
                new Student(4, "d")
        );
        List<StudentDTO> studentDTOList = CollectionUtils.merge(people, students, Person::getName, Student::getName,
                (person, student) -> {
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
        CollectionUtils.find(people, person -> person.getAge() == 111).ifPresent(System.out::println);
    }

    @Test
    public void zip() {
        Map<Integer, String> zip = CollectionUtils.zip(Lists.newArrayList(1, 2, 3), Lists.newArrayList("a", "b", "c"));
        System.out.println(zip);
    }


    @Test
    public void toMap() {
        List<Person> people = Lists.newArrayList(new Person(1, "hehe"), new Person(2, "haha"));
        Map<Integer, Person> personMap = CollectionUtils.toMap(people, Person::getAge);
        System.out.println(personMap);
    }

    @Test
    public void grouping() {
        List<Person> people = Lists.newArrayList(new Person(1, "hehe"), new Person(1, "haha"),new Person(2, "heihei"));
        Map<Integer, List<Person>> grouping = CollectionUtils.grouping(people, Person::getAge);
        System.out.println(grouping);
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
