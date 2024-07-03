package com.bajka.quizserwer.service;

import com.bajka.quizserwer.entity.Student;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class CurrentStudentService {

    private Student currentStudent;

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

}
