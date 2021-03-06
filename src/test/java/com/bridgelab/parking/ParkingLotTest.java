package com.bridgelab.parking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
public class ParkingLotTest {
    Vehicle vehicle;
    ParkingLot parkingLot,parkingLotOne ;
    ParkingLotSystem parkingLotSystem;
    ParkingLotOwner lotOwner;
    AirportSecurity airportSecurity;
    private Vehicle vehicle1;
    List carsExpectedList;

    @Before
    public void setUp() {
        vehicle =new Vehicle();
        parkingLotSystem = new ParkingLotSystem();
        parkingLot = new ParkingLot(2);
        vehicle =new Vehicle("White","Toyota","MH19 AB 2341");
        vehicle1 =new Vehicle("Yellow","Swift","MH20 GH 4563");
        carsExpectedList = new ArrayList();
        parkingLotSystem.getAddedLot(parkingLot);
        airportSecurity = new AirportSecurity();
        lotOwner = new ParkingLotOwner();
        parkingLotSystem.registeredObserver(lotOwner);
    }

    //added parking lot
    @Test
    public void givenParkingLot_WhenAdded_ShouldReturnTrue() {
        parkingLotOne = new ParkingLot(2);
        parkingLotSystem.getAddedLot(parkingLotOne);
        boolean lotAdded = parkingLotSystem.isAfterLotAdded(parkingLotOne);
        Assert.assertTrue(lotAdded);
    }

    @Test
    public void givenParkingLot_WhenAdded_ShouldReturnFalse() {
        parkingLotOne = new ParkingLot(2);
        boolean lotAdded = parkingLotSystem.isAfterLotAdded(parkingLotOne);
        Assert.assertFalse(lotAdded);
    }

    //UC1//
    @Test
    public void givenVehicle_WhenParkedInParkingLot_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        boolean isParked = parkingLotSystem.isVehicleParked(vehicle);
        assertTrue(isParked);
    }


    @Test
    public void givenVehicle_WhenAlReadyParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park(vehicle,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_ALREADY_PARKED,e.type);
        }
    }

    //UC2//
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        boolean isUnParked = parkingLotSystem.getUnParked(vehicle);
        Assert.assertTrue(isUnParked);
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() {
        boolean isUnParked = parkingLotSystem.getUnParked(vehicle);
        Assert.assertFalse(isUnParked);
    }

    //UC3//
    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheOwner() {
        parkingLotSystem.registeredObserver(lotOwner);
        try {
            parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(new Vehicle(), VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(new Vehicle(),VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        } catch (ParkingLotException e) {
            boolean capacityFull = lotOwner.isCapacityFull();
            assertFalse(capacityFull);
        }
    }
    @Test
    public void givenCapacityIs2_ShouldAbleToPark2Vehicle() {
        Vehicle vehicle2 = new Vehicle();
        parkingLotSystem.park(vehicle,  VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        boolean isParked1 = parkingLotSystem.isVehicleParked(vehicle);
        boolean isParked2 = parkingLotSystem.isVehicleParked(vehicle2);
        assertTrue(isParked1 && isParked2);
    }

    //UC4//
    @Test
    public void givenWhenLotIsFull_ShouldInformTheSecurity() {
        parkingLotSystem.registeredObserver(airportSecurity);
        try {
            parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(new Vehicle(),VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(new Vehicle(),VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle, VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(new Vehicle(), VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(new Vehicle(), VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
        } catch (ParkingLotException e) {
            boolean capacityFull = airportSecurity.isCapacityFull();
            Assert.assertFalse(capacityFull);
        }
    }

    //UC5//
    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheOwnerAndReturnFalse() {
        Vehicle vehicle2 = new Vehicle();
        parkingLotSystem.registeredObserver(lotOwner);
        parkingLotSystem.park(vehicle,  VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.getUnParked(vehicle);
        assertFalse(lotOwner.isCapacityFull());
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheAirPortSecurityAndReturnFalse() {
        parkingLotSystem.registeredObserver(airportSecurity);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.getUnParked(vehicle);
        assertFalse(airportSecurity.isCapacityFull());
    }

    //UC6
    @Test
    public void givenParkingLot_WhenInitialize_ShouldReturnParkingCapacity() {
        parkingLot.setCapacity(10);
        int parkingLotCapacity = parkingLot.initializeParkingLotForVehicle();
        Assert.assertEquals(10,parkingLotCapacity);
    }

    @Test
    public void givenParkingLot_ShouldReturnAvailableSlots() {
        carsExpectedList.add(0);
        carsExpectedList.add(1);
        parkingLot.setCapacity(2);
        ArrayList emptySlotList = parkingLot.getEmptySlotList();
        Assert.assertEquals(carsExpectedList, emptySlotList);
    }

    @Test
    public void givenParkingLot_WhenParkWithProvidedSlot_ShouldReturnTrue() {
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        boolean vehicleParked = parkingLotSystem.isVehicleParked(vehicle);
        Assert.assertTrue(vehicleParked);
    }

    @Test
    public void AfterParkAndUnParkVehicles_ShouldReturnEmptySlots() {
        carsExpectedList.add(0);
        carsExpectedList.add(2);
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.getUnParked(vehicle);
        ArrayList emptySlotList = parkingLot.getEmptySlotList();
        Assert.assertEquals(carsExpectedList, emptySlotList);
    }

    @Test
    public void givenVehicleForParkingOnEmptySlot_WhenParkWithProvidedEmptySlot_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        boolean vehiclePark = parkingLotSystem.isVehicleParked(vehicle);
        assertTrue(vehiclePark);
    }

    //UC7//
    @Test
    public void givenVehicle_WhenPresent_ShouldReturnSlot() {
        parkingLot.setCapacity(5);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        int vehicleSlot = parkingLotSystem.findVehicleInParkingLot(vehicle);
        Assert.assertEquals(0,vehicleSlot);
    }

    @Test
    public void givenVehicle_WhenNotPresent_ShouldReturnException() {
        try {
            parkingLot.setCapacity(5);
            parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.findVehicleInParkingLot(vehicle1);
        }catch (ParkingLotException e){
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND,e.type);
        }
    }

    //UC8//
    @Test
    public void givenVehicle_WhenParkWithTime_ShouldReturnParkingTime() {
        parkingLot.setCapacity(5);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        ParkingSlots parkingSlot=new ParkingSlots(vehicle);
        LocalTime time = parkingSlot.time;
        LocalTime timeOfVehicle = parkingLotSystem.getFindTimeForVehicle(vehicle);
        Assert.assertEquals(time,timeOfVehicle);
    }

    //UC9//
    @Test
    public void givenVehicle_WhenParkedAtEmptySlot_ShouldReturnTrue() {
        Vehicle vehicle2=new Vehicle();
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        boolean isVehicleParked = parkingLotSystem.isVehicleParked(vehicle);
        boolean isVehicleParked1 = parkingLotSystem.isVehicleParked(vehicle1);
        boolean isVehicleParked2 = parkingLotSystem.isVehicleParked(vehicle2);
        Assert.assertTrue(isVehicleParked&&isVehicleParked1&&isVehicleParked2);
    }

    //UC10//
    @Test
    public void givenDriverTypeHandicap_WhenParkedAtEmptySlot_ShouldReturnTrue() {
        Vehicle vehicle2=new Vehicle();
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle2,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        boolean isVehicleParked = parkingLotSystem.isVehicleParked(vehicle);
        boolean isVehicleParked1 = parkingLotSystem.isVehicleParked(vehicle1);
        boolean isVehicleParked2 = parkingLotSystem.isVehicleParked(vehicle2);
        Assert.assertTrue(isVehicleParked&&isVehicleParked1&&isVehicleParked2);
    }

    @Test
    public void givenDriverTypeHandicap_WhenParkedAtNearestSlot_ShouldReturnTrue() {
        Vehicle vehicle2=new Vehicle();
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        boolean isVehicleParked = parkingLotSystem.isVehicleParked(vehicle);
        boolean isVehicleParked1 = parkingLotSystem.isVehicleParked(vehicle1);
        boolean isVehicleParked2 = parkingLotSystem.isVehicleParked(vehicle2);
        Assert.assertTrue(isVehicleParked&&isVehicleParked1&&isVehicleParked2);
    }

    //UC11//
    @Test
    public void givenLargeVehicle_WhenParkByEvenlyDistribution_ShouldReturnTrue() {
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle,VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
        boolean isVehicleParked = parkingLotSystem.isVehicleParked(vehicle);
        Assert.assertTrue(isVehicleParked);
    }

    @Test
    public void givenLargeVehicle_WhenParkByEvenlyDistribution_ShouldReturnEmptySlotAfterLargeVehiclePark() {
        int expectedSlot=1;
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle,VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.isVehicleParked(vehicle);
        int emptySlot = parkingLotSystem.findVehicleInParkingLot(vehicle);
        Assert.assertEquals(expectedSlot,emptySlot);
    }

    //UC12//
    @Test
    public void givenParkingLot_WhenParkedWhiteVehicles_ShouldReturnListOfVehicles() {
        ArrayList<String> carsExpectedList=new ArrayList<>();
        Vehicle vehicle2 = new Vehicle("White","Swift","MH18 BN 78963");
        carsExpectedList.add("0 Toyota White MH19 AB 2341");
        carsExpectedList.add("2 Swift White MH18 BN 78963");
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle2,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        ArrayList sortedVehicleList = parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.COLOUR,"White");
        Assert.assertEquals(carsExpectedList, sortedVehicleList);
    }

    @Test
    public void givenParkingLot_WhenNoOneWhiteVehiclesParked_ShouldThrowException() {
        Vehicle vehicle2 = new Vehicle("Yellow","Swift Desire","MH17 OP 98765");
        Vehicle vehicle3 = new Vehicle("Black","Honda","MH18 BH 845621");
        parkingLot.setCapacity(3);
        try {
            parkingLotSystem.park(vehicle1, VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle2, VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle3, VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
            parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.COLOUR,"White");
        }catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND,e.type);
        }
    }


    //UC13//
    @Test
    public void givenVehicles_WhenParkedWithBlueColorAndToyota_ShouldReturnListOfPlateNumbers() {
        ArrayList<String> expectedNumber=new ArrayList<>();
        Vehicle vehicle2=new Vehicle("Blue","Toyota","MH19 NJ 56781");
        Vehicle vehicle3 =new Vehicle("Blue","Toyota","MH16 OP 123456");
        expectedNumber.add("0 Toyota Blue MH19 NJ 56781");
        expectedNumber.add("1 Toyota Blue MH16 OP 123456");
        parkingLotSystem.park(vehicle2,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle3,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        ArrayList<String> sortedVehicleList = parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.NAME_COLOUR,"Toyota","Blue");
        Assert.assertEquals(expectedNumber,sortedVehicleList);
    }

    @Test
    public void givenParkingLot_WhenParkedWithoutBlueColorAndToyota_ShouldThrowException() {
        Vehicle vehicle2 = new Vehicle("Red","Swift Desire","MH17 OP 98765");
        Vehicle vehicle3 = new Vehicle("Black","Honda","MH18 BH 845621");
        parkingLot.setCapacity(3);
        try {
            parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle3, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.NAME_COLOUR,"Toyota","Blue");
        }catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND,e.type);
        }
    }

    //UC14//
    @Test
    public void givenParkingLot_WhenParkedABMWVehicle_ShouldReturnListOfSlots() {
        ArrayList<String> expectedList=new ArrayList<>();
        Vehicle vehicle2 = new Vehicle("White","BMW","MH18 BN 78963");
        Vehicle vehicle3 = new Vehicle("Black","BMW","MH20 TY 01210");
        expectedList.add("0 BMW White MH18 BN 78963");
        expectedList.add("2 BMW Black MH20 TY 01210");
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle3,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        ArrayList sortedVehicleList = parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.NAME,"BMW");
        Assert.assertEquals(expectedList, sortedVehicleList);
    }

    @Test
    public void givenParkingLot_WhenNoOneBMWVehicleParked_ShouldThrowException() {
        Vehicle vehicle2 = new Vehicle("Red","Swift Desire","MH17 OP 98765");
        Vehicle vehicle3 = new Vehicle("Black","Honda","MH18 BH 845621");
        parkingLot.setCapacity(3);
        try {
            parkingLotSystem.park(vehicle1, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle2, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle3, VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.NAME,"BMW");
        }catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND,e.type);
        }
    }

    //UC15
    @Test
    public void givenParkingLot_WhenParkedBefore30Minutes_ShouldReturnListOfSlots() {
        Vehicle vehicle2 = new Vehicle("White","BMW","MH18 BN 78963");
        Vehicle vehicle3 = new Vehicle("Black","BMW","MH20 TY 01210");
        carsExpectedList.add("0 BMW White MH18 BN 78963");
        carsExpectedList.add("1 Swift Yellow MH20 GH 4563");
        carsExpectedList.add("2 BMW Black MH20 TY 01210");
        parkingLot.setCapacity(3);
        parkingLotSystem.park(vehicle2,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle1, VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle3,VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
        ArrayList sortedVehicleList = parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.TIME_IN_MINUTES,"30");
        Assert.assertEquals(carsExpectedList, sortedVehicleList);
    }

    @Test
    public void givenParkingLot_WhenNoOneVehicleParkedBefore30Minutes_ShouldReturnException() {
        Vehicle vehicle2 = new Vehicle("White","BMW","MH18 BN 78963");
        try {
            parkingLotSystem.park(vehicle2, VehicleType.SMALL, EnumDriverType.NORMALDRIVER);
            parkingLotSystem.getUnParked(vehicle2);
            parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.TIME_IN_MINUTES,"30");
        }catch (ParkingLotException e){
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND,e.type);
        }
    }

    //UC16//
    @Test
    public void givenDriverTypeAndVehicleType_WhenVehicleParked_ShouldReturnDetails() {
        List<String> expectedList=new ArrayList<>();
        Vehicle vehicle2 = new Vehicle("White","BMW","MH18 BN 78963");
        Vehicle vehicle3 = new Vehicle("Yellow","Swift","MH20 TY 01210");
        Vehicle vehicle4 = new Vehicle("Wine","Honda","MH19 BO 4920");
        Vehicle vehicle5 = new Vehicle("Black","Volvo","MH24 AE 84235");
        Vehicle vehicle6 = new Vehicle("Blue","Suzuki","MH17 UH 32541");
        parkingLot.setCapacity(10);
        expectedList.add("0 Toyota White MH19 AB 2341");
        expectedList.add("2 Swift Yellow MH20 TY 01210");
        expectedList.add("4 Volvo Black MH24 AE 84235");
        expectedList.add("5 Suzuki Blue MH17 UH 32541");
        parkingLotSystem.park(vehicle,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        parkingLotSystem.park(vehicle2,VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle3,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        parkingLotSystem.park(vehicle4,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle5,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        parkingLotSystem.park(vehicle6,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        List<String> vehiclesDetails = parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.DRIVER_VEHICLE_TYPE,"SMALL","HANDICAPDRIVER");
        Assert.assertEquals(expectedList,vehiclesDetails);
    }

    @Test
    public void givenDriverTypeAndVehicleType_WhenVehicleNotParked_ShouldReturnException() {
        Vehicle vehicle2 = new Vehicle("White","BMW","MH18 BN 78963");
        Vehicle vehicle4 = new Vehicle("Wine","Honda","MH19 BO 4920");
        parkingLot.setCapacity(10);
        try {
            parkingLotSystem.park(vehicle2,VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.park(vehicle4,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
            parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.DRIVER_VEHICLE_TYPE, "SMALL", "HANDICAPDRIVER");
        }catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND,e.type);
        }
    }

    //uc17
    @Test
    public void givenParkingLots_WhenFindParkedVehicles_ShouldReturnVehiclesDetails() {
        List<String> expectedList=new ArrayList<>();
        Vehicle vehicle2 = new Vehicle("White","BMW","MH18 BN 78963");
        Vehicle vehicle3 = new Vehicle("Yellow","Swift","MH20 TY 01210");
        Vehicle vehicle4 = new Vehicle("Wine","Honda","MH19 BO 4920");
        Vehicle vehicle5 = new Vehicle("Black","Volvo","MH24 AE 84235");

        parkingLot.setCapacity(10);
        expectedList.add("0 Toyota White MH19 AB 2341");
        expectedList.add("1 BMW White MH18 BN 78963");
        expectedList.add("2 Swift Yellow MH20 TY 01210");
        expectedList.add("3 Honda Wine MH19 BO 4920");
        expectedList.add("4 Volvo Black MH24 AE 84235");
        parkingLotSystem.park(vehicle,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        parkingLotSystem.park(vehicle2,VehicleType.LARGE,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle3,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        parkingLotSystem.park(vehicle4,VehicleType.SMALL,EnumDriverType.NORMALDRIVER);
        parkingLotSystem.park(vehicle5,VehicleType.SMALL,EnumDriverType.HANDICAPDRIVER);
        List<String> vehiclesDetails = parkingLotSystem.searchVehiclesByGivenFields(VehicleSortedCatagories.ALL_PARKED);
        Assert.assertEquals(expectedList,vehiclesDetails);
    }
}