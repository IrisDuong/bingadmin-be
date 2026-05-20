package com.bing.auth.controller;

import lombok.Builder;
import lombok.Data;

@Builder
public record AuthenticatedUser(String name,String email,String phoneNo,String picture) {

}
