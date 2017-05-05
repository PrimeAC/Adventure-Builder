package pt.ulisboa.tecnico.softeng.activity.services.local;


import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;

import java.util.ArrayList;
import java.util.List;

public class ActivityProviderInterface {
	@Atomic(mode = Atomic.TxMode.READ)
	public static List<ActivityProviderData> getActivityProviders() {
		List<ActivityProviderData> providers = new ArrayList<>();
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			providers.add(new ActivityProviderData(provider, CopyDepth.SHALLOW));
		}
		return providers;
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public static void createProvider(ActivityProviderData activityProviderData) {
		new ActivityProvider(activityProviderData.getCode(), activityProviderData.getName());
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public static ActivityProviderData getActivityProviderDataByCode(String activityProviderCode, CopyDepth depth) {
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

}
