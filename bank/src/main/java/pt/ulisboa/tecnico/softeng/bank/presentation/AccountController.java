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

@Controller
@RequestMapping(value = "/banks/{code}/account/{iban}")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String accountForm(Model model, @PathVariable String iban, @PathVariable String code) {
		logger.info("accountForm");

		AccountData accountData = new AccountData();
		accountData.setBankCode(code);
		accountData.setIban(iban);
		accountData.setBalance(BankInterface.getBalance(iban));

		model.addAttribute("account", accountData);
		model.addAttribute("balance", BankInterface.getBalance(iban));
		logger.info("test iban:{}, balance:{}, code:{}" , iban, BankInterface.getBalance(iban), code);
		return "account";
	}

	@RequestMapping(method = RequestMethod.POST, params="action=Deposit")
	public String accountSubmitD(Model model, @PathVariable String iban, @PathVariable String code, @ModelAttribute AccountData accountData) {
		logger.info("accountSubmit iban:{}, amount:{}" , accountData.getIban(), accountData.getAmount());

		try {
			BankInterface.deposit(accountData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible do proceed with the Deposit");
			model.addAttribute("account", accountData);
			return "account";
		}

		return "redirect:/banks/" + code + "/account/" + iban;
	}

	@RequestMapping(method = RequestMethod.POST, params="action=Withdraw")
	public String accountSubmitW(Model model, @PathVariable String iban, @PathVariable String code, @ModelAttribute AccountData accountData) {
		logger.info("accountSubmit iban:{}, amount:{}" , accountData.getIban(), accountData.getAmount());

		try {
			BankInterface.widthraw(accountData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to proceed with the Withdraw");
			model.addAttribute("account", accountData);
			return "account";
		}

		return "redirect:/banks/" + code + "/account/" + iban;
	}
}
