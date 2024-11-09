package Banking;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank {
	private ArrayList<Account> accounts;
	private Scanner scanner;
	private Database database;
	

    public Bank() {
    	accounts = new ArrayList<>();
    	scanner = new Scanner(System.in);
    	database = new Database();
    	loadAccountsFromDatabase();
    }
    
    
    public void start() {
    	while(true) {
    		System.out.println("\n----- Welcome to the Banking Application -----");
    		System.out.println("1. Create Account");
    		System.out.println("2. Deposit");
    		System.out.println("3. Withdraw");
    		System.out.println("4. Check Balance");
    		System.out.println("5. View Transaction History");
    		System.out.println("6. Transfer Funds");
    		System.out.println("7. Generate Report");
    		System.out.println("8. Update");
    		System.out.println("9. Exit");
    		System.out.print("Choose an option: ");
    		int choice = scanner.nextInt();
    		
    		switch(choice) {
    			case 1 :
    				createAccount();
    				break;
    			case 2 :
    				deposit();
    				break;
    			case 3 :
    				withdraw();
    				break;
    			case 4 :
    				viewAccount();
    				break;
    			case 5 :
    				viewTransactionHistory();
    				break;
    			case 6 :
    				transferFunds();
    				break;
    			case 7 :
    				generateReportsMenu();
    				break;
    			case 8 :
    				System.out.print("Enter the Account ID to Update: ");
    				int accountId = scanner.nextInt();
    				updateCustomerDetails(accountId);
    				break;
    			case 9:
    				System.out.println("😀Happy Banking😀\n✌Visit Again✌");
    				database.closeConnection();
    				System.exit(0);
    				break;
    			default :
    				System.out.println("Choose a valid option!");
    		}
    	}
    }


	private void createAccount() {
		System.out.print("Enter customer ID: ");
        long customerId = scanner.nextLong();
        String.valueOf(customerId).length();
        if (String.valueOf(customerId).length()<8 || String.valueOf(customerId).length()>16) {
            System.out.println("Customer ID must be 8-16 digits.");
            return;  
        }		
	
		System.out.print("Enter the account type('s' for Savings and 'c' for Current account): ");
		char accountTypeIn = scanner.next().charAt(0);
		String accountType;
		if(accountTypeIn == 's' || accountTypeIn == 'S') {
			accountType = "Savings";
		}
		else if(accountTypeIn == 'c' || accountTypeIn == 'C') {
			accountType = "Current";
		}
		else {
			System.out.println("Invalid account type entered. Defaulting to Savings account!");
			accountType = "Savings";
		}
		scanner.nextLine();
		
		
		System.out.print("Enter your Name: ");
		String name = scanner.nextLine();
		//scanner.nextLine();
		
		System.out.print("Enter your Phone Number: ");
		String phoneNumber = scanner.nextLine();
		//scanner.nextLine();
		
		if(String.valueOf(phoneNumber).length()!=10) {
			System.out.println("Enter valid phone number: ");
			return;
		}
		System.out.print("Enter youe Address: ");
		String address = scanner.nextLine();
		
		Account account = new Account(customerId,accountType,name,phoneNumber,address);
		database.insertAccount(account);
		System.out.println("Account created successfully!"+"\nYour Account ID is: "+account.getAccountId());
		loadAccountsFromDatabase();
		}
	private void deposit() {
		System.out.println("Enter Account ID: ");
		int accountId = scanner.nextInt();
		Account account = findAccount(accountId);
		if(account!= null) {
			System.out.print("Enter the amount to deposit: ");
			double amount = scanner.nextDouble();
			account.deposit(amount);
			database.updateAccountBalance(account);
			System.out.println("Deposit successful! \nNew Balance: "+account.getBalance());
		}
		else {
			System.out.println("Account not found");
		}
		System.out.println("------------------------------------------------------------------------------------------------------");
		scanner.nextLine();
	}
	private void withdraw() {
		System.out.print("Enter Account ID: ");
		int accountId = scanner.nextInt();
		Account account = findAccount(accountId);
		if(account!= null) {
			System.out.print("Enter the amount to withdraw: ");
			double amount = scanner.nextDouble();
			if(account.withdraw(amount)) {
			database.updateAccountBalance(account);
			System.out.println("Withdrew successful! New Balance: "+account.getBalance());
			}else {
				System.out.println("Insufficient funds");
			}
		}
			
		else {
			System.out.println("Account not found");
		}
		System.out.println("------------------------------------------------------------------------------------------------------");
		scanner.nextLine();
	}
	private void viewAccount() {
		System.out.println("Enter Account ID: ");
		int accountId = scanner.nextInt();
		Account account = findAccount(accountId);
		if(account!= null) {
			System.out.println("Account ID: "+account.getAccountId());
			System.out.println("Customer ID: "+account.getCustomerId());
			System.out.println("Account Type: "+account.getAccountType());
			System.out.println("Name: "+account.getName());
			System.out.println("Phone Number: "+account.getphoneNumber());
			System.out.println("Address: "+account.getAddress());
			System.out.println("Balance: "+account.getBalance());
		}
		else {
			System.out.println("Account not found");
		}
		System.out.println("------------------------------------------------------------------------------------------------------");
		scanner.nextLine();
	}
	
	private void viewTransactionHistory() {
		System.out.print("Enter Account ID: ");
		int accountId = scanner.nextInt();
		Account account = findAccount(accountId);
		if(account!= null) {
			account.printTransactionHistory();
		}
		else {
			System.out.println("Account not found");
		}
		System.out.println("------------------------------------------------------------------------------------------------------");
		scanner.nextLine();
	}
	
	private void transferFunds(){
		System.out.print("Enter Sender Account ID: ");
		int accountId = scanner.nextInt();
		Account account = findAccount(accountId);
		
		if(account == null) {
			System.out.println("Receiver account not found");
			return;
		}
		
		System.out.print("Enter Receiver Account ID to transfer funds: ");
		int t_accountId = scanner.nextInt();
		Account t_account = findAccount(t_accountId);
		
			if(t_account == null) {
				System.out.println("Receiver account not found");
				return;
			}
			
			System.out.print("Enter the amount to be transfer: ");
			double amount = scanner.nextDouble();
				
			if(amount <= 0) {
					System.out.println("Transfer amount must be positive!");
					return;
				}
			
			if(account.getBalance()<amount) {
				System.out.println("Insufficient funds in sender account.Available balance is " + account.getBalance() );
				return;
			}
			
			account.withdraw(amount);
			t_account.deposit(amount);
			database.updateAccountBalance(account);
			database.updateAccountBalance(t_account);
			
			System.out.printf("Successfully transferred %.2f from Account Id : %d to Account Id %d.%n",amount,accountId,t_accountId);
			System.out.println("------------------------------------------------------------------------------------------------------");
			scanner.nextLine();
				
	}
	
	private Account findAccount(int accountId){
		for(Account acc : accounts) {
		if(acc.getAccountId()==accountId) {
			return acc;
			}	
		}
		return null;
	}
	
	private void generateReportsMenu() {
		while(true) {
			System.out.println("1. Customer Report");
			System.out.println("2. Total Account type");
			System.out.println("3. Total Amount");
			System.out.println("4. Exit");
			
			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			
			switch(choice) {
			case 1 :
				customerReport();	
				break;
				
			case 2 :
				totalAccountType();
				break;
				
			case 3 :
				totalAmount();
				break;
			
			case 4 :
				return;
			
			default :
				System.out.println("Enter a valid choice!");
				System.out.println("------------------------------------------------------------------------------------------------------");
				scanner.nextLine();
			}
		}
	}
	
	private void customerReport() {
		ArrayList<Account> accounts = database.getAllAccounts();
		 System.out.println("Customer Details Report");
		 System.out.println("------------------------------------------------------------------------------------------------------");
		 System.out.printf(String.format("%-10s %-12s %-15s %-10s %-20s %-15s %-10s%n", 
				 "Account Id", "Customer Id","Account Type","Balance","Name","Phone Number","Address"));
		 System.out.println("------------------------------------------------------------------------------------------------------");
	        for (Account account : accounts) {
	            System.out.printf(String.format("%-10s %-12s %-15s %-10s %-20s %-15s %-10s%n",
	            		account.getAccountId(),
	            		account.getCustomerId(),
	            		account.getAccountType(),
	            		account.getBalance(),
	            		account.getName(),
	            		account.getphoneNumber(),
	            		account.getAddress()));
	        }
		 System.out.println("------------------------------------------------------------------------------------------------------");
	}
	
	private void totalAccountType() {
		int totalSavingsAccount=0,totalCurrentAccount=0;
		for(Account account : accounts) {
			if(account.getAccountType().equalsIgnoreCase("Savings")) {
				totalSavingsAccount += 1;
			}
			if(account.getAccountType().equalsIgnoreCase("Current")) {
				totalCurrentAccount += 1;
			}
		}
		System.out.println("Total Savings Accounts: "+ totalSavingsAccount);
		System.out.println("Total Current Accounts: "+ totalCurrentAccount);
		System.out.println("------------------------------------------------------------------------------------------------------");
		scanner.nextLine();
	}
	
	private void totalAmount() {
		double sum = 0.0;
		for(Account account : accounts) {
			sum += account.getBalance();
			}
		System.out.println("Total Balance in all account is: "+ sum);
		
	System.out.println("------------------------------------------------------------------------------------------------------");
	scanner.nextLine();
	}
	
	private void updateCustomerDetails(int accountId) {
		Account account = findAccount(accountId);
		if(account != null) {
			System.out.println("Customer details");
			System.out.println("Name: " + account.getName());
			System.out.println("Phone number: " + account.getphoneNumber());
			System.out.println("Address: " + account.getAddress());
		}
		else {
			System.out.println("Account not found");
		}
			
			System.out.print("Enter New Name(or press enter to keep current): ");
			scanner.nextLine();
			String name = scanner.nextLine();
			if(!name.isEmpty()) {
				account.setName(name);
			}
			
			System.out.print("Enter New Phone number: ");
			String phoneNumber = scanner.nextLine();
			if(!phoneNumber.isEmpty()) {
				account.setPhoneNumber(phoneNumber);
			}
			
			System.out.print("Enter New Address: ");
			String address = scanner.nextLine();
			if(!address.isEmpty()) {
				account.setAddress(address);
			}
			database.updateAccountDetails(account);
			//System.out.println("Details Updated Successfully!!!");
			System.out.println("----------------------------------------------------------------------");
			
			
		}
	private void loadAccountsFromDatabase() {
		accounts = database.getAllAccounts();
	}

}