package pt.ulisboa.tecnico.softeng.activity.services.local;

import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;

import java.util.ArrayList;
import java.util.List;

public class ActivityInterface {

	@Atomic(mode = Atomic.TxMode.READ)
	public static List<ActivityProviderData> getActivityProviders() {
		List<ActivityProviderData> providers = new ArrayList<>();
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			providers.add(new ActivityProviderData(provider, ActivityProviderData.CopyDepth.SHALLOW));
		}
		return providers;
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public static void createProvider(ActivityProviderData activityProviderData) {
		new ActivityProvider(activityProviderData.getCode(), activityProviderData.getName());
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public static ActivityProviderData getActivityProviderDataByCode(String activityProviderCode, ActivityProviderData.CopyDepth depth) {
		ActivityProvider provider = getActivityProviderByCode(activityProviderCode);

		if (provider != null) {
			return new ActivityProviderData(provider, depth);
		} else {
			return null;
		}
	}

	private static ActivityProvider getActivityProviderByCode(String code) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (provider.getCode().equals(code)) {
				return provider;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null) {
			return booking.cancel();
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityReservationData getActivityReservationData(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					Booking booking = offer.getBooking(reference);
					if (booking != null) {
						return new ActivityReservationData(provider, offer, booking);
					}
				}
			}
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityData getActivityData(String code) {
		Activity activity = getActivityByCode(code);
		if (activity != null) {
			return new ActivityData(getActivityByCode(code), ActivityData.CopyDepth.OFFERS);
		} else {
			return null;
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public static boolean createOffer(String activityCode, ActivityOfferData offerData) {
		Activity activity = getActivityByCode(activityCode);
		try{
			if (activity != null) {
				new ActivityOffer(activity, offerData.getBegin(), offerData.getEnd());
				return true;
			} else {
				return false;
			}
		} catch (ActivityException ignore){
			return false;
		}
	}

	private static Booking getBookingByReference(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createActivity(String providerCode, ActivityData activityData) {
		new Activity(getActivityProviderByCode(providerCode), activityData.getName(), activityData.getMinAge(), activityData.getMaxAge(),
				activityData.getCapacity());
	}

	private static Activity getActivityByCode(String code) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Activity activity = provider.getActivity(code);
			if (activity != null) {
				return activity;
			}
		}
		return null;
	}

}
