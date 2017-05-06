package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{code}/clients")
public class ClientController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String clientForm(Model model, @PathVariable String code) {
		logger.info("clientForm");


		BankData bankData = BankInterface.getBankData(code, BankData.CopyDepth.OPERATIONS);

		if (bankData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code " + code);
			model.addAttribute("bank", new BankData());
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		} else {
			ClientData clientData = new ClientData();
			//clientData.setBank(BankInterface.getBankByCode(code));
			model.addAttribute("client", clientData);
			model.addAttribute("clients", BankInterface.getClients(code));
			model.addAttribute("bank", bankData);
			model.addAttribute("operations", bankData.getBankOperations());
		}
		return "clients";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bankSubmit(Model model, @ModelAttribute ClientData clientData, @PathVariable String code) {
		logger.info("clientSubmit name:{}, bank:{}", clientData.getName(), clientData.getBank());

		try {
			BankInterface.createClient(clientData);
		} catch (BankException be) {
			BankData bankData = BankInterface.getBankData(code, BankData.CopyDepth.OPERATIONS);
			model.addAttribute("error", "Error: it was not possible to create the client");
			model.addAttribute("client", clientData);
			model.addAttribute("clients", BankInterface.getClients(code));
			model.addAttribute("bank", bankData);
			model.addAttribute("operations", bankData.getBankOperations());
			return "clients";
		}

		return "redirect:/banks/" + code + "/clients";
	}
}