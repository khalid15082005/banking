package Banking;
import java.sql.*;
import java.util.*;

public class Database {
		private Connection connection;
		
		public Database() {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","kh@lid");
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		public void insertAccount(Account account) {
			String sql = "insert into accounts (customer_id,name,phone_number,account_type,address,balance)"
					+ "values(?,?,?,?,?,?)" ;
			try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root", "kh@lid");
					PreparedStatement psmt = conn.prepareStatement(sql))
					{
						psmt.setLong(1, account.getCustomerId());
						psmt.setString(2, account.getName());
						psmt.setString(3, account.getphoneNumber());
						psmt.setString(4, account.getAccountType());
						psmt.setString(5, account.getAddress());
						psmt.setDouble(6, account.getBalance());
						psmt.executeUpdate();
						
					}catch(SQLException e) {
						System.out.println(e.getMessage());
					}
		}
		
		
		public void updateAccountBalance(Account account) {
			try {
				String query = "Update accounts set balance = ? where account_id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setDouble(1, account.getBalance());
				statement.setInt(2, account.getAccountId());
				statement.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ArrayList<Account> getAllAccounts(){
			ArrayList<Account>accounts = new ArrayList<>();
			try {
				String query ="Select * from accounts";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				while(resultSet.next()) {
					int accountId = resultSet.getInt("account_id");
					long customerId = resultSet.getLong("customer_id");
					double balance = resultSet.getDouble("balance");
					String accountType = resultSet.getString("account_type");
					String name = resultSet.getString("name");
					String phonenumber = resultSet.getString("phone_number");
					String address = resultSet.getString("address");
					
					Account account = new Account(customerId,accountType,name,phonenumber,address);
					account.setAccountId(accountId);
					account.deposit(balance); 
					accounts.add(account);
				}
			}catch(SQLException e) {
					e.printStackTrace();	
				}
			return accounts;
		}
		
		public void updateAccountDetails(Account account) {
			String query = "Update accounts set name = ? , phone_number = ?, address= ? where account_id = ?";
			try (PreparedStatement psmt = connection.prepareStatement(query)){
				psmt.setString(1, account.getName());
				psmt.setString(2, account.getphoneNumber());
				psmt.setString(3, account.getAddress());
				psmt.setInt(4, account.getAccountId());
				
				int rowsUpdated = psmt.executeUpdate();
				if(rowsUpdated > 0) {
					System.out.println("Accounts details updated successfully");
				}
				else {
					System.out.println("Account Id not found");
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		

		
		public void closeConnection() {
			try {
				if(connection != null) {
					connection.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
}