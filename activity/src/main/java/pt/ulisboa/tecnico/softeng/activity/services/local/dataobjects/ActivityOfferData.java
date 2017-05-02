package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;

public class ActivityOfferData {
	private LocalDate begin, end;
	private int capacity;

	public ActivityOfferData() {
	}

	public ActivityOfferData(ActivityOffer offer) {
		begin = offer.getBegin();
		end = offer.getEnd();
		capacity = offer.getCapacity();
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
