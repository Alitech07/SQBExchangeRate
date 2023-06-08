package org.example.entity;

import com.sun.org.apache.bcel.internal.generic.LNEG;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String step;
    private Long photoId;
}
