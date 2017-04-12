package com.shaun.fyp.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.shaun.fyp.model.Patient;
import com.shaun.fyp.resource.beans.PatientFilterBean;
import com.shaun.fyp.service.PatientService;


/*
 * This is the Patient Resource it is used to do execute the HTTP Methods related to the patient.
 */
@Path("/patients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {
	
	PatientService patientService = new PatientService();
	
	
	/*
	 * This is a GET Method that is used to Get either all patients,
	 * or a patient by name if a name is specified as a query parameter
	 */
	@GET
	public Response getPatients(@BeanParam PatientFilterBean filterBean,  @Context UriInfo uriInfo) {
		
		//if name is query param
		if(!filterBean.getName().isEmpty())
		{
			System.out.println("in name query param if");
			List<Patient> patientList = patientService.getPatientByName(filterBean.getName());
			if(!patientList.isEmpty())
			{
				for(Patient p : patientList)
				{
					p.addLink(getUriForSelf(uriInfo, p), "self");
				}
				return Response.ok()
						.entity(patientList)
						.build();
			}
			else 
				return Response.status(Status.NOT_FOUND).build();
			
		}
		//if start or size is query param
		if(filterBean.getStart() >= 0 && filterBean.getSize()  > 0)
		{
			List<Patient> sortedPatientList = patientService.getAllPatientsPaginated(filterBean.getStart(), filterBean.getSize());
			System.out.println("in name query param if");
			if(sortedPatientList != null)
			{
				for(Patient p : sortedPatientList)
				{
					p.addLink(getUriForSelf(uriInfo, p), "self");
				}
				return Response.ok()
						.entity(sortedPatientList)
						.build();
			}
			else 
				return Response.status(Status.NOT_FOUND).build();
		}
		System.out.println("in getPatients()");
		
		//Default get all
		List<Patient> patientList = patientService.getAllPatients();
		if(patientList != null)
		{
			for(Patient p : patientList)
			{
				p.addLink(getUriForSelf(uriInfo, p), "self");
			}
			return Response.ok()
					.entity(patientList)
					.build();
		}
		else 
			return Response.status(Status.NOT_FOUND).build();
	}
	
	/*
	 * This is a GET method to get a single patient with the ID specified in the Path
	 */
	@GET
	@Path("/{id}")
	public Response getPatientById(@PathParam("id") String id, @Context UriInfo uriInfo)
	{
		Patient thePatient = patientService.getPatientById(id);
		if(thePatient != null) {
			thePatient.addLink(getUriForSelf(uriInfo, thePatient), "self");
			return Response.ok()
					.entity(thePatient)
					.build();
		}
		else 
			return Response.status(Status.NOT_FOUND).build();
	}

	
	/*
	 * This is a POST Method that creates a new patient
	 * The patient parameter is the message body of the request
	 * Jersey does this conversion from JSON to a Patient
	 */
	@POST
	public Response addPatient(Patient patient, @Context UriInfo uriInfo)
	{
		Patient newPatient = patientService.addPatient(patient);
		if(newPatient != null)
		{
			newPatient.addLink(getUriForSelf(uriInfo, newPatient), "self");
			URI uri = uriInfo.getAbsolutePathBuilder().path(newPatient.get_id()).build();
			return Response.created(uri)
					.entity(newPatient)
					.build();
		}
		else
			return Response.status(Status.CONFLICT).build();
	}
	
	/*
	 * This is a POST method that accepts a List of patients in JSON
	 * This adds this list of patients to the Database
	 */
	@POST
	@Path("/list")
	public List<Patient> addPatientList(List<Patient> patients)
	{
		System.out.println("in addPatientList()");
		return patientService.addPatientList(patients);
	}
	
	
	/*
	 * This is a PUT Method that updates the patient as the specified ID
	 * The patient parameter is the message body of the request
	 * Jersey does this conversion from JSON to a Patient
	 */
	@PUT
	@Path("/{id}")
	public Patient updatePatient(@PathParam("id") String id, Patient patient)
	{
		patient.set_id(id);
		return patientService.updatePatient(patient);
	}
	
	/*
	 * This is a DELETE Method that deletes the patient with the ID specified in the path
	 */
	@DELETE
	@Path("/{id}")
	public void deletePatient(@PathParam("id") String id)
	{
		System.out.println("In deletePatientResource");
		patientService.deletePatient(id);
	}
		
	/*
	 * This is used to return the URI of the resource with the ID of the patient appended onto it.
	 * This is how HATEOAS was applied to the RESTAPI
	 */
	private String getUriForSelf(UriInfo uriInfo, Patient thePatient) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(PatientResource.class)
				.path(thePatient.get_id())
				.build()
				.toString();
		return uri;
	}
}
