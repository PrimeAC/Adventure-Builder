package pt.ulisboa.tecnico.softeng.activity.presentation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;

@Controller
@RequestMapping(value = "/activities/{activityCode}")
public class ActivityOfferController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String offerForm(Model model, @PathVariable String activityCode) {
		logger.info("adventureForm");

		ActivityData activity = ActivityInterface.getActivityData(activityCode);
		if (activity == null) {
			model.addAttribute("error", "No Activity with such code: " + activityCode);
			return "redirect:/"; // TODO CHANGE this
		} else {
			model.addAttribute("offer", new ActivityOfferData());
			model.addAttribute("activity", activity);
			return "offers";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createOffer(Model model, @PathVariable String activityCode,
	                          @ModelAttribute ActivityOfferData offerData) {
		logger.info("createOffer");

		boolean created = ActivityInterface.createOffer(activityCode, offerData);

		if (created) {
			return "redirect:/activities/" + activityCode;
		} else {
			model.addAttribute("error", "Unable to create offer for activity: " + activityCode);
			return "redirect:/"; // TODO CHANGE this
		}
	}


}
