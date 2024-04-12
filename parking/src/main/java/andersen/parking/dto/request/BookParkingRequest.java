package andersen.parking.dto.request;

import java.time.LocalDateTime;

public record BookParkingRequest(int parkingId, int carId, int spaceId, LocalDateTime startTime, LocalDateTime endTime) {

}
