package com.bridgelab.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLot {

    private int actualCapacity;
   // private List vehicles;
    public List<Object> vehicles;
    private List<ParkingLotObserver> parkLotObserver;
    private int slot = 0;
   // private ParkingLotObserver parkingInfo;

    public ParkingLot(int capacity) {
       /* this.vehicles = new ArrayList();
        this.actualCapacity = capacity;*/

        setCapacity(capacity);
        this.parkLotObserver = new ArrayList<>();
    }

    public void registerParkingLotOwner(AirportSecurity observer) {
       // this.parkingInfo = owner;
        this.parkLotObserver.add(observer);
    }

    public  void setCapacity(int capacity) {
        this.actualCapacity = capacity;
        initializeParkingLot();
    }

    public int initializeParkingLot() {
        this.vehicles=new ArrayList<>();
        IntStream.range(0,this.actualCapacity).forEach(slots ->vehicles.add(null));
        return vehicles.size();
    }


    public ArrayList<Integer> getSlot() {
        ArrayList<Integer> emptySlots = new ArrayList<>();
        for (int slot = 0; slot < this.actualCapacity; slot++) {
            if (this.vehicles.get(slot) == null)
                emptySlots.add(slot);
        }
        return emptySlots;
    }

    /*public void registerSecurity(AirportSecurity airportSecurity) {
        this.parkingInfo = airportSecurity;
    }*/

    public void park(Object vehicle) throws ParkingLotException {
        if(isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked");
        if (vehicles.size() == actualCapacity && !vehicles.contains(null)) {
            for (ParkingLotObserver observer : parkLotObserver)
                observer.capacityIsFull();
            throw new ParkingLotException("parkinglot is full");
    }
        parked(slot++, vehicle);
    }

    public void parked(int slot, Object vehicle) throws ParkingLotException {
        if (isVehicleParked(vehicle)) {
            throw new ParkingLotException("VEHICLE ALREADY PARK");
        }
        this.vehicles.set(slot, vehicle);
    }

    public boolean isVehicleParked(Object vehicle) {
        if (this.vehicles.contains(vehicle))
            return true;
        return false;
    }

    public boolean getUnParked(Object vehicle) {
        if ( vehicle == null)  return false;
        if (this.vehicles.contains(vehicle)) {
            //this.vehicles.remove(vehicle);
            this.vehicles.set(this.vehicles.indexOf(vehicle), null);
            return true;
        }
        return false;
    }

    //find a vehicle if it is parked//
    public int findVehicleInParkingLot(Object vehicle) {
        if(!this.vehicles.contains(vehicle))
            throw new ParkingLotException("Vehicle Is Absent");
        return this.vehicles.indexOf(vehicle);
    }
}
