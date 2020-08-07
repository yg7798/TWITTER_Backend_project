package tech.codingclub.helix.entity;

public class TimeAPI {
    private String time_str;
    private Long epoch_time;

    public TimeAPI()
    {

    }
    public TimeAPI(String time_str, Long epoch_time) {
        this.time_str = time_str;
        this.epoch_time = epoch_time;
    }



}
