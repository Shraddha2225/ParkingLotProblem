package com.bridgelab.parking;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean isFullcapacity;

    @Override
    public void capacityIsFull() {
        isFullcapacity = true;
    }

    @Override
    public void setCapacityIsFull() {

    }

    @Override
    public void setParkingLotAvailable() {

    }

    public boolean isCapacityFull() {
        return this.isFullcapacity;
    }

}
