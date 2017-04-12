package com.shaun.fyp.service;

import java.util.List;

import com.shaun.fyp.dao.PatientDao;
import com.shaun.fyp.model.Patient;

/*
 * This is the patient service layer.
 * This adds an extra layer between the PatientResource & PatientDao.
 * Provides a better separation of concerns
 */
public class PatientService {

	PatientDao patientDao = PatientDao.getInstance();
	
	public List<Patient> getAllPatients()
	{
		return patientDao.getAllPatients();
	}
	
	public Patient getPatientById(String _id) {
		return patientDao.getPatientById(_id);
		
	}

	public Patient addPatient(Patient patient) {
		return patientDao.addPatient(patient);
	}

	public Patient updatePatient(Patient patient) {
		return patientDao.updatePatient(patient);
	}

	public List<Patient> getPatientByName(String name) {
		return patientDao.getPatientsByName(name);
	}

	public void deletePatient(String id) {
		patientDao.deletePatient(id);
		
	}

	public List<Patient> addPatientList(List<Patient> patients) {
		return patientDao.addPatientList(patients);
	}

	public List<Patient> getAllPatientsPaginated(int start, int size) {
		return patientDao.getAllPatientsPaginated(start, size);
	}

}
