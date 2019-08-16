package com.personiv.portal.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtAuthenticationResponse {
    private  String access_token;
    private  String token_type;
    private  Date expiration;
 
}

