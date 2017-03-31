package pt.ulisboa.tecnico.softeng.activity.dataobjects;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityReservationData {
	private String reference;
	private String cancellation;
	private String name;
	private String code;
	private LocalDate begin;
	private LocalDate end;
	private LocalDate cancellationDate;

	public ActivityReservationData(String reference, String code, String name, LocalDate begin,
	                               LocalDate end) {
		checkArguments(reference, code, name, begin, end);
		this.reference = reference;
		this.cancellation = null;
		this.code = code;
		this.name = name;
		this.begin = begin;
		this.end = end;
		this.cancellationDate = null;

	}

	public void checkArguments(String reference, String code, String name, LocalDate begin,
	                           LocalDate end) {
		if(reference == null || reference.trim().equals("") || code == null || code.trim().equals("") ||
				name == null || name.trim().equals("") || begin == null || end == null){
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
}
