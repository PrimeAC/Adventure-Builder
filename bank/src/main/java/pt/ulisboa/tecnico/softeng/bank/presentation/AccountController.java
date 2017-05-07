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
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{code}/clients/{id}/accounts/{iban}/account")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String accountForm(Model model, @PathVariable String iban, @PathVariable String code, @PathVariable String id) {
		logger.info("accountForm");

		BankData bankData = BankInterface.getBankData(code, BankData.CopyDepth.SHALLOW);
		ClientData clientData = BankInterface.getClientData(id, code, ClientData.CopyDepth.SHALLOW);
		AccountData accountData = BankInterface.getAccountDataByIban(iban);

		model.addAttribute("account", accountData);
		model.addAttribute("client", clientData);
		model.addAttribute("bank", bankData);
		model.addAttribute("operation", new BankOperationData());
		model.addAttribute("balance", accountData.getBalance());
		logger.info("iban:{}, client:{}, bank:{}", accountData.getIBAN(), clientData.getId(), bankData.getCode());
		return "account";
	}

	@RequestMapping(method = RequestMethod.POST, params = "action=Deposit")
	public String accountSubmitD(Model model, @PathVariable String iban, @PathVariable String code,
	                             @PathVariable String id, @ModelAttribute BankOperationData bankOperationData,
	                             @ModelAttribute BankData bankData, @ModelAttribute ClientData clientData) {
		logger.info("SubmitForm");

		AccountData accountData = BankInterface.getAccountDataByIban(iban);

		logger.info("iban:{}, amount:{}", accountData.getIBAN(), bankOperationData.getValue());

		try {
			BankInterface.deposit(bankOperationData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible do proceed with the Deposit");
			model.addAttribute("operation", bankOperationData);
			model.addAttribute("bank", bankData);
			model.addAttribute("client", clientData);
			model.addAttribute("account", accountData);

			return "account";
		}

		return "redirect:/banks/" + code + "/clients/" + id + "/accounts/" + iban + "/account";
	}

	@RequestMapping(method = RequestMethod.POST, params = "action=Withdraw")
	public String accountSubmitW(Model model, @PathVariable String iban, @PathVariable String code,
	                             @PathVariable String id, @ModelAttribute BankOperationData bankOperationData,
	                             @ModelAttribute BankData bankData, @ModelAttribute ClientData clientData) {
		logger.info("SubmitForm");

		AccountData accountData = BankInterface.getAccountDataByIban(iban);

		logger.info("iban:{}, amount:{}", accountData.getIBAN(), bankOperationData.getValue());

		try {
			BankInterface.widthraw(bankOperationData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible do proceed with the Deposit");
			model.addAttribute("operation", bankOperationData);
			model.addAttribute("bank", bankData);
			model.addAttribute("client", clientData);
			model.addAttribute("account", accountData);

			return "account";
		}

		return "redirect:/banks/" + code + "/clients/" + id + "/accounts/" + iban + "/account";
	}
}