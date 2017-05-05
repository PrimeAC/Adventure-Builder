package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;


import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

import java.util.ArrayList;
import java.util.List;

public class ActivityProviderData {
	public static enum CopyDepth {
		SHALLOW, ACTIVITY
	}


	private String name;
	private String code;
	private List<ActivityData> activities = new ArrayList<>();

	public ActivityProviderData() {
	}

	public ActivityProviderData(ActivityProvider provider, CopyDepth depth) {
		this.name = provider.getName();
		this.code = provider.getCode();

		switch (depth) {
			case ACTIVITY:
				for (ACTIVITY activity : provider.getActivitySet()) {
					this.activities.add(new ActivityData(activity));
				}
				break;
			case SHALLOW:
				break;
			default:
				break;
		}

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

	public List<ActivityData> getActivities() {
		return this.activities;
	}

	public void setActivities(List<ActivityData> activities) {
		this.activities = activities;
	}

}
