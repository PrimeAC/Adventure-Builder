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
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{code}/clients/{id}/accounts")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String accountForm(Model model, @PathVariable String code, @PathVariable String id) {
		logger.info("accountForm");

		BankData bankData = BankInterface.getBankData(code, BankData.CopyDepth.CLIENTS);
		ClientData clientData = BankInterface.getClientData(id, code, ClientData.CopyDepth.ACCOUNTS);


		if (clientData == null) {
			model.addAttribute("error", "Error: it does not exist a client with the id " + id);
			model.addAttribute("client", new ClientData());
			model.addAttribute("clients", BankInterface.getClients(code));
			return "clients";
		} else {
			model.addAttribute("account", new AccountData());
			model.addAttribute("client", clientData);
			model.addAttribute("bank", bankData);
			return "accounts";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String accountSubmit(Model model, @ModelAttribute AccountData accountData, @PathVariable String code,
								@PathVariable String ID) {

		try {
			BankInterface.createAccount(accountData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the account");
			model.addAttribute("account", accountData);
			model.addAttribute("client", BankInterface.getClientData(ID, code, ClientData.CopyDepth.ACCOUNTS));
			return "accounts";
		}
		return "redirect:/banks/" + code + "/clients/" + ID + "/accounts";
	}

	@RequestMapping(value = "/account/{iban}", method = RequestMethod.POST, params = "action=Deposit")
	public String accountSubmitD(Model model, @PathVariable String IBAN, @PathVariable String code,
								 @PathVariable String ID, @ModelAttribute AccountData accountData) {
		logger.info("accountSubmit iban:{}, amount:{}", accountData.getIBAN(), accountData.getAmount());

		try {
			BankInterface.deposit(accountData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible do proceed with the Deposit");
			model.addAttribute("account", accountData);
			return "account";
		}

		return "redirect:/banks/" + code + "/clients/" + ID + "/accounts" + "/account/" + IBAN;
	}

	@RequestMapping(value = "/account/{iban}", method = RequestMethod.POST, params = "action=Withdraw")
	public String accountSubmitW(Model model, @PathVariable String IBAN, @PathVariable String code,
								 @PathVariable String ID, @ModelAttribute AccountData accountData) {
		logger.info("accountSubmit iban:{}, amount:{}", accountData.getIBAN(), accountData.getAmount());

		try {
			BankInterface.widthraw(accountData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to proceed with the Withdraw");
			model.addAttribute("account", accountData);
			return "account";
		}

		return "redirect:/banks/" + code + "/clients/" + ID + "/accounts" + "/account/" + IBAN;
	}
}
