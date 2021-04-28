package by.tc.library.controller.command;

import java.util.HashMap;
import java.util.Map;

import by.tc.library.controller.command.impl.*;

public class CommandProvider {
	private Map<CommandName, Command> commands = new HashMap<>();
	
	public CommandProvider() {
		commands.put(CommandName.ADDNEWBOOK, new AddNewBook());
		commands.put(CommandName.CHANGELOCALRU, new ChangeLocalRU());
		commands.put(CommandName.CHANGELOCALEN, new ChangeLocalEN());
		commands.put(CommandName.CONFIRMBOOKRETURN, new ConfirmBookReturn());
		commands.put(CommandName.CONFIRMORDECLINEORDER, new ConfirmOrDeclineOrder());
		commands.put(CommandName.CHANGEBOOK, new ChangeBook());
		commands.put(CommandName.CHANGEPASSWORD, new ChangePassword());
		commands.put(CommandName.DELETEBOOK, new DeleteBook());
		commands.put(CommandName.FINDBOOKBYNAME, new FindBook());
		commands.put(CommandName.GIVEBOOKTOREADER, new GiveBookToReader());
		commands.put(CommandName.GOTOBOOKPAGE, new GoToBookPage());
		commands.put(CommandName.GOTOINDEXPAGE, new GoToIndexPage());
		commands.put(CommandName.GOTOMAINPAGE, new GoToMainPage());
		commands.put(CommandName.GOTOREADERACCOUNT, new GoToReaderAccount());
		commands.put(CommandName.LOGINATION, new Logination());
		commands.put(CommandName.LOGOUT, new Logout());
		commands.put(CommandName.REGISTRATEORDER, new RegistrateOrder());
		commands.put(CommandName.REGISTRATION, new GoToRegistrationPage());
		commands.put(CommandName.RETURNBOOK, new ReturnBook());
		commands.put(CommandName.SAVENEWUSER, new SaveNewUser());
		commands.put(CommandName.SIGNIN, new SignIn());
		commands.put(CommandName.SHOWBOOKSTOLIB, new ShowBooksToLib());
		commands.put(CommandName.SHOWBOOKSTORETURN, new ShowBooksToReturn());
		commands.put(CommandName.SHOWORDERSTOLIB, new ShowOrdersToLib());
		commands.put(CommandName.SHOWPOPULAR, new ShowPopular());
		commands.put(CommandName.SHOWREADERHISTORY, new ShowReaderHistory());
		commands.put(CommandName.SHOWREADERORDERS, new ShowReaderOrders());
		commands.put(CommandName.SHOWRETURNSTOLIB, new ShowReturnsToLib());
	}
	
	
	public Command takeCommand(String name) {
		CommandName commandName;
		
		commandName = CommandName.valueOf(name.toUpperCase());
		
		return commands.get(commandName);
	}

}
