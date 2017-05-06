package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;


import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

import java.util.ArrayList;

public class ActivityData {
	public static enum CopyDepth {
		SHALLOW, OFFERS
	};

	private ActivityProvider provider;
	private String name;
	private String code;
	private int minAge;
	private int maxAge;
	private int capacity;

	private List<ActivityOfferData> offers = new ArrayList<>();

	public ActivityData(){

	}

	public ActivityData(Activity activity, CopyDepth depth) {
		this.provider = activity.getActivityProvider();
		this.name = activity.getName();
		this.code = activity.getCode();
		this.minAge = activity.getMinAge();
		this.maxAge = activity.getMaxAge();
		this.capacity = activity.getCapacity();

		switch (depth) {
			case OFFERS:
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					this.offers.add(new ActivityOfferData(offer, ActivityOfferData.CopyDepth.SHALLOW));
				}
				break;
			case SHALLOW:
				break;
			default:
				break;
		}
	}

	public ActivityProvider getActivityProvider() {
		return this.provider;
	}

	public void setActivityProvider(ActivityProvider provider) {
		this.provider = provider;
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

	public int getMinAge() {
		return this.minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return this.maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public List<ActivityOfferData> getOffers() {
		return this.offers;
	}

	public void setOffers(List<ActivityOfferData> offers) {
		this.offers = offers;
	}
}
