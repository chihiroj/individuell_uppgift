package com.example.individuell_uppgift.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class is used for returning a success or error message.
 */
@Data
@AllArgsConstructor
public class MessageResponseDTO {
    private String message;
}
