package com.commuting.commutingapp.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNameRequest {
    private String id;
    private String firstName;
    private String lastName;
}
