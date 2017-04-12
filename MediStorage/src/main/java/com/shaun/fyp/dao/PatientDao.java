package com.shaun.fyp.dao;

import static com.mongodb.client.model.Filters.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shaun.fyp.model.Patient;

import utils.SystemUtils;

/*
 * This is the DAO or Data Access Object for the Patient Collection
 */
public class PatientDao {

	private static PatientDao patientDao = null;	
	
	private MongoClient mongoClient;
	
	/*
	 *  These are the details of the database I was using on my local machine before I started using ec2
	 */
			// Local DB Info
//			private String dbName = "DB1";
//			private String collectionName = "Patients";		
	
	
	/*
	 * This is how I connected to the MongoDB database in ec2
	 */
	//Private IP Address of ec2 Instance with MongoDB installed on it
	private String dbServer =  SystemUtils.getMongoDB_EC2_Private_IP();
	
	private String ec2DbName = "mediStorageDB";
	private String ec2CollName = "mediStorageDB";
	
	private String username = "shaungilbert";
	private String password = "samcro";
	
	private MongoCollection<Document> coll;
	
	private PatientDao()
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
	public static PatientDao getInstance()
	{
		if(patientDao == null)
		{
			patientDao = new PatientDao();
		}
		
		return patientDao;
	}
	
	/*
	 * This method is used to get a full list of patients from the Database
	 */
	public List<Patient> getAllPatients()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Patient> list = new ArrayList<Patient>();
		for (Document doc: coll.find()) {
			try {
				list.add(mapper.readValue(doc.toJson(), Patient.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/*
	 * This Method is used to paginate the patients returned
	 */
	public List<Patient> getAllPatientsPaginated(int start, int size) { 
		ObjectMapper mapper = new ObjectMapper();
		List<Patient> list = new ArrayList<Patient>();
		for (Document doc: coll.find().skip(start).limit(size)) {
			try {
				list.add(mapper.readValue(doc.toJson(), Patient.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/*
	 * This method is used to get a list of patients with the specified name
	 */
	public List<Patient> getPatientsByName(String name)
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Patient> list = new ArrayList<Patient>();
		List<Document> docs = coll.find(eq("name", name)).into(new ArrayList<Document>());
		
		//multiple params
//		List<Document> docs = coll.find(and(eq("name", name), eq("illness", "Foot Injury"))).into(new ArrayList<Document>());
		
		for(Document doc : docs)
		{
			try {
				list.add(mapper.readValue(doc.toJson(), Patient.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(list == null)
			return null;
		return list;

	}
	
	/*
	 * This method is used to get a patient with the specified id
	 */
	public Patient getPatientById(String _id)
	{
		ObjectMapper mapper = new ObjectMapper();
		
		Document doc = coll.find(eq("_id", _id)).first();
		if(doc == null)
			return null;
		try {
			return mapper.readValue(doc.toJson(), Patient.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * This method is used to add a patient to the database
	 */
	public Patient addPatient(Patient patient) {
		if(getPatientById(patient.get_id()) != null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		
		String json = null;
		try {
			json = mapper.writeValueAsString(patient);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(json!=null)
			coll.insertOne(Document.parse(json));
		return patient;
	}
	
	/*
	 * This method is used to add a list of patients to the database
	 */
	public List<Patient> addPatientList(List<Patient> patients) {
		ObjectMapper mapper = new ObjectMapper();
		List<Document> docList = new ArrayList<Document>();
		for(Patient p : patients)
		{
			try {
				docList.add(Document.parse(mapper.writeValueAsString(p)));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}			
		}
		if(!docList.isEmpty())
			coll.insertMany(docList);
		
		return patients;
	}


	/*
	 * This method is used to Update the passed in patient
	 */
	public Patient updatePatient(Patient patient) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(patient);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(json!=null)
			coll.updateOne(eq("_id", patient.get_id()), new Document("$set", Document.parse(json)));
		return patient;
	}

	/*
	 * This method is used to delete the patient with the ID specified
	 */
	public void deletePatient(String id) {
		coll.deleteOne(eq("_id", id));
	}

}
