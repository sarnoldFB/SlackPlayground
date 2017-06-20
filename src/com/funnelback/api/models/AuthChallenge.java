package com.funnelback.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "token",
        "challenge",
        "type"
})
public class AuthChallenge {
    @JsonProperty("token")
    public String token;
    @JsonProperty("challenge")
    public String challenge;
    @JsonProperty("type")
    public String type;
}