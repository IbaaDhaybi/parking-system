package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TicketDAOTest {

	private TicketDAO ticketDao;

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
		ticketDao = new TicketDAO();
		ReflectionTestUtils.setField(ticketDao, "dataBaseConfig", dataBaseConfig);
		
	}

	@Test
	public void saveTicketTest() throws Exception {
		// Creeer un ticket
		Ticket ticket = new Ticket();
		// set les attributs du ticket
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);

		Date outTime = new Date();
		ticket.setOutTime(outTime);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setParkingSpot(parkingSpot);

		ticket.setVehicleRegNumber("RD234");
		new FareCalculatorService().calculateFare(ticket);

		// Avant de sauvegarder le ticket : il faut assurer que la connexion vers la
		// base de données est bonne

		when(dataBaseConfig.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(stmt);

		// sauvegarder le ticket via TicketDAO objet la fonction saveTicket
		ticketDao.saveTicket(ticket);

		// Verifier que le ticket est bien sauvegardé
		verify(connection, Mockito.times(1)).prepareStatement(DBConstants.SAVE_TICKET);
	}

	@Test
	public void getTicketTest() throws Exception {

		// Creeer un ticket
		Ticket ticket = new Ticket();
		// set les attributs du ticket
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);

		Date outTime = new Date();
		ticket.setOutTime(outTime);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setParkingSpot(parkingSpot);

		ticket.setVehicleRegNumber("DR123");
		new FareCalculatorService().calculateFare(ticket);

		// Avant de sauvegarder le ticket : il faut assurer que la connexion vers la
		// base de données est bonne

		when(dataBaseConfig.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(stmt);

		// sauvegarder le ticket via TicketDAO objet la fonction saveTicket
		ticketDao.saveTicket(ticket);
		LocalDateTime in date;
		when(stmt.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(Boolean.TRUE);
		when(rs.getString(6)).thenReturn("CAR");
        when(rs.getBoolean(7)).thenReturn(Boolean.FALSE);
        when(rs.getInt(2)).thenReturn(1);
        when(rs.getDouble(3)).thenReturn(1D);
        when(rs.getTimestamp(4).toInstant()).thenReturn();
        when(rs.getTimestamp(5).toLocalDateTime()).thenReturn(outTime);
        when(rs.getBoolean(8)).thenReturn(Boolean.FALSE);
        String vehiculeNumber ="vehiculeNumber";

		// récupérer le ticket via TicketDAO objet la fonction getTcket
		ticketDao.getTicket("DR123");

		// Verifier que le ticket est bien récupéré
		verify(connection, Mockito.times(1)).prepareStatement(DBConstants.GET_TICKET);

	}

	@Test
	public void updateTicketTest() throws Exception {

		// Creeer un ticket
		Ticket ticket = new Ticket();
		// set les attributs du ticket
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);

		Date outTime = new Date();
		ticket.setOutTime(outTime);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setParkingSpot(parkingSpot);

		ticket.setVehicleRegNumber("RD234");
		new FareCalculatorService().calculateFare(ticket);

		// Avant de sauvegarder le ticket : il faut assurer que la connexion vers la
		// base de données est bonne

		when(dataBaseConfig.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(stmt);

		// sauvegarder le ticket via TicketDAO objet la fonction saveTicket
		ticketDao.saveTicket(ticket);

		// récupérer le ticket via TicketDAO objet la fonction getTicket
		ticketDao.getTicket("RD234");

		// mettre à jour le ticket via TicketDAO objet la fonction updateTicket
		ticketDao.updateTicket(ticket);

		// Verifier que le ticket est bien mis à jour
		verify(connection, Mockito.times(1)).prepareStatement(DBConstants.UPDATE_TICKET);
	}
}