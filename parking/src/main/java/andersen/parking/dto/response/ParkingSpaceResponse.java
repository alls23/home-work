package andersen.parking.dto.response;

import andersen.parking.model.ParkingSpace;

public record ParkingSpaceResponse(int id, int parkingId, boolean isOccupied) {
    public static ParkingSpaceResponse fromModel(ParkingSpace parkingSpace) {
        return new ParkingSpaceResponse(parkingSpace.getId(), parkingSpace.getParkingId(), parkingSpace.isOccupied());
    }
}
