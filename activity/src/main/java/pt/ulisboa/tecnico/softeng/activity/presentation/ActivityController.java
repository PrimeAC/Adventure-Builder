package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.bank.exception.ActivityProviderException;
import pt.ulisboa.tecnico.softeng.bank.services.local.ActivityProviderInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ActivityProviderData;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController {
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityForm(Model model) {
		logger.info("activityForm");
		model.addAttribute("provider", new ActivityProviderData());
		model.addAttribute("providers", ActivityProviderInterface.getActivityProvider());
		return "providers";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bankSubmit(Model model, @ModelAttribute ActivityProviderData activityProviderData) {
		logger.info("ActivityProviderSubmit name:{}, code:{}", activityProviderData.getName(), activityProviderData.getCode());

		try {
			ActivityProviderInterface.createActivityProvider(activityProviderData);
		} catch (ActivityProviderException be) {
			model.addAttribute("error", "Error: it was not possible to create the provider");
			model.addAttribute("provider", activityProviderData);
			model.addAttribute("providers", ActivityProviderInterface.getActivityProvider());
			return "providers";
		}

		return "redirect:/activity";
	}
}

