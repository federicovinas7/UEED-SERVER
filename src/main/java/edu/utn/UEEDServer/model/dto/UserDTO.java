package edu.utn.UEEDServer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String name;
    private String surname;
    private Boolean employee;
}
