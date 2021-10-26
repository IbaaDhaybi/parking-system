package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ParkingSpotDAOTest {

	private ParkingSpotDAO parkingSpotDao;

	@Mock
	public DataBaseConfig dataBaseConfig;

	@Mock
	private Connection connection;

	@Mock
	private PreparedStatement stmt;

	@Mock
	private ResultSet rs;

	@BeforeEach
	public void setup() {
		parkingSpotDao = new ParkingSpotDAO();
		parkingSpotDao.dataBaseConfig = dataBaseConfig;
	}

	@Test
	public void getNextAvailableSlotTest() throws Exception {

		when(dataBaseConfig.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(stmt);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(1);
		when(stmt.executeQuery()).thenReturn(rs);

		int availableSlot = parkingSpotDao.getNextAvailableSlot(ParkingType.BIKE);

		assertEquals(availableSlot, 1);
		verify(connection, Mockito.times(1)).prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
	}

	@Test
	public void updateParkingTest() throws Exception {

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);

		when(dataBaseConfig.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(stmt);

		parkingSpotDao.updateParking(parkingSpot);

		verify(connection, Mockito.times(1)).prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
	}

	@Test
	public void verifyParkingAvailabilityTest() throws Exception {

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);

		when(dataBaseConfig.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(stmt);

		parkingSpotDao.verifyParkingAvailability(parkingSpot);

		verify(connection, Mockito.times(1)).prepareStatement(DBConstants.VERIFY_SPOT_AVAILABILITY);
	}
}