package com.example.demo.security.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDTO {
    private long id;
    private String questionCode;
    private String title;
    private String picture;
    private boolean isMultipleAns;

}
