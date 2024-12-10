package OnlineBookingSystem.OnlineBookingSystem.service.Impl;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimetableResponse {
    private int sequence;          // Sequence number
//    private String stationCode;    // Station code
    private String stationName;     // Station name
    private LocalTime arrivalTime;  // Arrival time
    private LocalTime departureTime; // Departure time
//    private double distance;        // Distance in kilometers
}