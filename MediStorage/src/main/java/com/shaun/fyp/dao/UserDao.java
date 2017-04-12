package com.shaun.fyp.dao;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shaun.fyp.model.User;

import utils.SystemUtils;

/*
 * This is the DAO or Data Access Object for the User Collection
 */
public class UserDao {
	
private static UserDao usersDao = null;	
	
	private MongoClient mongoClient;
	
	/*
	 *  These are the details of the database I was using on my local machine before I started using ec2
	 */
				// Local DB Info
//	private String dbName = "DB1";
//	private String collectionName = "Users";
	
	/*
	 * This is how I connected to the MongoDB database in ec2
	 */
	//Private IP Address of ec2 Instance with MongoDB installed on it
	private String dbServer =  SystemUtils.getMongoDB_EC2_Private_IP();

	private String ec2DbName = "mediStorageDB";
	private String ec2CollName = "UsersDB";
	
	private String username = "shaungilbert";
	private String password = "samcro";
	
	private MongoCollection<Document> coll;
	
	private UserDao()
	{
		
		/*
		 * This is how I connect to MongoDB on ec2 instance
		 */
									// ec2 Connection
		MongoClientURI uri = new MongoClientURI("mongodb://" + username + ":" + password + "@" + dbServer + "/" + ec2DbName); 
		mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase(ec2DbName);
		coll = db.getCollection(ec2CollName);
		
		/*
		 * This is how I was connecting to mongoDB on my local machine before I was using ec2
		 */
					// local connection
//		mongoClient = new MongoClient("localhost", 27017);
//		MongoDatabase db = mongoClient.getDatabase(dbName);
//		coll = db.getCollection(collectionName);
		
	}
	
	/*
	 * Singleton Pattern
	 */
	public static UserDao getInstance()
	{
		if(usersDao == null)
		{
			usersDao = new UserDao();
		}
		
		return usersDao;
	}
	
	/*
	 * This method is used to get the user with the specified userName
	 */
	public User getUserByUserName(String userName)
	{
		ObjectMapper mapper = new ObjectMapper();
		
		Document doc = coll.find(eq("userName", userName)).first();
		if(doc == null)
			return null;
		try {
			return mapper.readValue(doc.toJson(), User.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * This method is used to create a new user
	 */
	public User addUser(User newUser) {
		if(getUserByUserName(newUser.getUserName()) != null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		
		String json = null;
		try {
			json = mapper.writeValueAsString(newUser);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(json!=null)
			coll.insertOne(Document.parse(json));
		return newUser;
	}
	
}
