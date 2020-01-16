package com.bookstore.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.entity.Customer;

public class CustomerServices {

		
	private CustomerDAO customerDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	public CustomerServices(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}
	
	public void listCustomers(String message) throws ServletException, IOException {
		
		if(message != null) {
			request.setAttribute("message", message);
		}
		
		 customerDAO = new CustomerDAO();
		
		List<Customer> listCustomer = customerDAO.listAll();
		
		
		
		request.setAttribute("listCustomer", listCustomer);
		
		String listPage = "customer_list.jsp";
		
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(listPage);
		
		requestDispatcher.forward(request, response);
		
	}
	
	public void listCustomers() throws ServletException, IOException {
		
		listCustomers(null);
	}
	
	
	
	public void createCustomer() throws ServletException, IOException {
		
		String email = request.getParameter("email");
		
		
		Customer existCustomer = customerDAO.findByEmail(email);
		
		if(existCustomer != null) {
			
			String message = "Could not create customer : the email : " + email + " is already register by customer";
			
			listCustomers(message);
			
		}else {
			
			String fullName = request.getParameter("fullName");
			String password = request.getParameter("password");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String zipcode = request.getParameter("zipcode");
			String country = request.getParameter("country");
			
			Customer newCustomer  = new Customer();
			newCustomer.setFullname(fullName);
			newCustomer.setPassword(password);
			newCustomer.setPhone(phone);
			newCustomer.setAddress(address);
			newCustomer.setCity(city);
			newCustomer.setZipcode(zipcode);
			newCustomer.setCountry(country);
			
			customerDAO.create(newCustomer);
			
			String message = "new customer has been created successsfully";
			listCustomers(message);
			
			
		}
		
	}
	
}