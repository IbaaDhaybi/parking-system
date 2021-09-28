package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;

public class FareCalculatorService {

	public static final int critereReccurent = 3;

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		double duration = (outHour - inHour) / 3600000d;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}

		// stationnement gratuit pour les 30 premières minutes
		if (duration <= 0.5) {
			ticket.setPrice(0);
		} else {

			// réduction de 5 % pour les utilisateurs récurrents
			TicketDAO ticketDAO = new TicketDAO();
			if (ticketDAO.recurrentClient(ticket.getVehicleRegNumber()) >= critereReccurent) {
				double reduction = 0.05 * ticket.getPrice();
				ticket.setPrice(ticket.getPrice() - reduction);
			}

		}

	}
}