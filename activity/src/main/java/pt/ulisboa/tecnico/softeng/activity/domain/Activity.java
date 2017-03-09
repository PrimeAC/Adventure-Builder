package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Activity {
	private static int counter = 0;

	private final String name;
	private final String code;
	private final int minAge;
	private final int maxAge;
	private final int capacity;
	private final Set<ActivityOffer> offers = new HashSet<>();

	public Activity(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		if (provider == null || isBlank(name))
			throw new ActivityException("Inconsistent arguments");

		checkAge(minAge, maxAge);
		checkCapacity(capacity);

		this.code = provider.getCode() + Integer.toString(++Activity.counter);
		this.name = name;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.capacity = capacity;

		provider.addActivity(this);
	}

	private void checkAge(int minAge, int maxAge) {
		if (minAge < 18 || maxAge >= 100 || minAge > maxAge)
			throw new ActivityException("Age doesn't match to the requirements");
	}

	private void checkCapacity(int capacity) {
		if (capacity <= 0)
			throw new ActivityException("Invalid capacity");
	}

	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getMinAge() {
		return this.minAge;
	}

	int getMaxAge() {
		return this.maxAge;
	}

	int getCapacity() {
		return this.capacity;
	}

	int getNumberOfOffers() {
		return this.offers.size();
	}

	void addOffer(ActivityOffer offer) {
		this.offers.add(offer);
	}

	/**
	 * Checks if the {@link Activity} has some {@link ActivityOffer} that is overlapped
	 * by the the period between begin and end values.
	 */
	boolean hasOffers(LocalDate begin, LocalDate end) {
		for (ActivityOffer offer : this.offers) {
			if (offer.matchDateConflict(begin, end)) {
				return true;
			}
		}
		return false;
	}

	Set<ActivityOffer> getOffers(LocalDate begin, LocalDate end, int age) {
		Set<ActivityOffer> result = new HashSet<>();
		for (ActivityOffer offer : this.offers) {
			if (matchAge(age) && offer.available(begin, end)) {
				result.add(offer);
			}
		}
		return result;
	}

	boolean matchAge(int age) {
		return age >= this.minAge && age <= this.maxAge;
	}

}
