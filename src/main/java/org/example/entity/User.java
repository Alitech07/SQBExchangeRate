package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String pasportSeria;
    private String phoneNumber;
    private String step;
    private Integer photoId;
    private double salary;

    public User(String id,String username, String step) {
        this.id = id;
        this.username = username;
        this.step = step;
    }
}
