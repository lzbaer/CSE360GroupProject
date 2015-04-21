package patient_backend;

public class User {
	
	private String name;
	private String password;
	
	public User(String n, String p)
	{
		name = n;
		password = p;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPassword()
	{
		return password;
	}
	

}
