package com.example.healthsystem.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "BMI is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "BMI must be greater than 0")
    private Double bmi;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be a positive number")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    private Integer age;

    @NotNull(message = "Birth date is required")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private String birthDate;
}
