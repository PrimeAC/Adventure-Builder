package pt.ulisboa.tecnico.softeng.activity.presentation.;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

@Controller
@RequestMapping(value = "/activities")
public class ActivityController {
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showActivities(Model model, @PathVariable String providerCode) {
		logger.info("showActivities code:{}", providerCode);

		ActivityProviderData providerData = ActivityProviderInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES);

		if (providerData == null) {
			model.addAttribute("error", "Error: it does not exist an activity provider with the code " + providerCode);
			model.addAttribute("provider", new ActivityProviderData());
			model.addAttribute("providers", ActivityProviderInterface.getActivityProviders());
			return "providers";
		} else {
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("provider", ActivityProviderData);
			return "activities";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitActivity(Model model, @PathVariable String providerCode, @ModelAttribute ActivityData activityData) {
		logger.info("activitySubmit providerCode:{}, name:{}, minAge:{}, maxAge:{}, capacity:{}", providerCode,
				activityData.getName(), activityData.getMinAge(), activityData.getMaxAger(), activityData.getCapacity());

		try {
			ActivityProviderInterface.createActivity(providerCode, activityData);
		} catch (ActivityException ae) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("activity", activityData);
			model.addAttribute("activities", ActivityProviderInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES));
			return "activities";
		}

		return "redirect:/providers/" + providerCode +"/activities";
	}
}