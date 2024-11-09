package Banking;

import java.util.ArrayList;

public class Account {
	private static int accountCounter = 1;
	private int accountId;
	private long customerId;
	private String accountType;
	private double balance;
	private String name;
	private String phoneNumber;
	private String address;
	private ArrayList<String> transactionHistory;

	public Account(long customerId, String accountType, String name, String phoneNumber, String address) {
		this.accountId = accountCounter ++;
		this.customerId = customerId;
		this.accountType = accountType;
		this.balance = 0.0;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.transactionHistory = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getphoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getAccountId() {
		return accountId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public String getAccountType() {
		return accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			transactionHistory.add("Deposited: " + amount);
		}
	}

	public boolean withdraw(double amount) {
		if (amount > 0 && balance >= amount) {
			balance -= amount;
			transactionHistory.add("Withdrew: " + amount);
			return true;
		}
		return false;
	}

	public void printTransactionHistory() {
		System.out.println("Transaction History for Account ID: " + accountId);
		for (String transaction : transactionHistory) {
			System.out.println(transaction);
		}
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}