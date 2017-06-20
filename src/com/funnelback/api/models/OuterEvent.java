package com.funnelback.api.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "token",
        "team_id",
        "api_app_id",
        "event",
        "type",
        "authed_users",
        "event_id",
        "event_time"
})
public class OuterEvent {
    @JsonProperty("token")
    public String token;
    @JsonProperty("team_id")
    public String teamId;
    @JsonProperty("api_app_id")
    public String apiAppId;
    @JsonProperty("event")
    public Event event;
    @JsonProperty("type")
    public String type;
    @JsonProperty("authed_users")
    public List<String> authedUsers = null;
    @JsonProperty("event_id")
    public String eventId;
    @JsonProperty("event_time")
    public Long eventTime;
}