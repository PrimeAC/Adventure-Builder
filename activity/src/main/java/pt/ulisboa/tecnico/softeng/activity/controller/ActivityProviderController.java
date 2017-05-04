package pt.ulisboa.tecnico.softeng.activity.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

@Controller
@RequestMapping(value = "/activityProvider")
public class ActivityProviderController {
	private static Logger logger = LoggerFactory.getLogger(ActivityProviderController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityProviderForm(Model model) {
		logger.info("providerForm");
		model.addAttribute("provider", new ActivityProvider());
		model.addAttribute("providers", ActivityProvider.providers);
		return "activityProvider";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String activityProviderSubmit(Model model, @ModelAttribute ActivityProvider provider) {
		logger.info("activityProviderSubmit name:{}, code:{}", provider.getName(), provider.getCode());

		try {
			new ActivityProvider(provider.getName(), provider.getCode());
		} catch (ActivityException ae) {
			model.addAttribute("error", "Error: it was not possible to create the activity provider");
			model.addAttribute("provider", provider);
			model.addAttribute("providers", ActivityProvider.providers);
			return "activityProvider";
		}

		return "redirect:/activityProvider";
	}
}
