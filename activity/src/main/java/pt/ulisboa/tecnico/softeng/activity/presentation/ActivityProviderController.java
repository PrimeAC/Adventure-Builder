package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.bank.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.services.local.ActivityProviderInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ActivityProviderData;

@Controller
@RequestMapping(value = "/providers")
public class ActivityProviderController {
	private static Logger logger = LoggerFactory.getLogger(ActivityProviderController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String ActivityProviderForm(Model model) {
		logger.info("ActivityProviderForm");
		model.addAttribute("provider", new ActivityProviderData());
		model.addAttribute("providers", ActivityProviderInterface.getActivityProviders());
		return "providers";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String activityProviderSubmit(Model model, @ModelAttribute ActivityProviderData activityProviderData) {
		logger.info("activityProviderSubmit name:{}, code:{}", activityProviderData.getName(), activityProviderData.getCode());

		try {
			ActivityProviderInterface.createProvider(activityProviderData);
		} catch (ActivityException be) {
			model.addAttribute("error", "Error: it was not possible to create the bank");
			model.addAttribute("provider", activityProviderData);
			model.addAttribute("providers", ActivityProviderInterface.getActivityProviders());
			return "providers";
		}

		return "redirect:/providers";
	}
}
