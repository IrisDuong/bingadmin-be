package com.bing.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record AuthenticatedUserDTO(String firstName,String lastName,String email,String picture) {

}
