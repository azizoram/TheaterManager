package cz.cvut.fel.workshiftservice;

import cz.cvut.fel.workshiftservice.model.WorkShift;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkshiftserviceApplication {

    public static void main(String[] args) {

//        WorkShift workShift = WorkShift.builder().id(1L).name("test").start(null).end(null).capacity(1).build();

        SpringApplication.run(WorkshiftserviceApplication.class, args);
    }

}
