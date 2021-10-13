package com.commuting.commutingapp.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipientInformation {
    String email;
    String name;
    String code;
}
