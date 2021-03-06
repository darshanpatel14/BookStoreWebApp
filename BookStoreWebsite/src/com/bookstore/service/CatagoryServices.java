package com.bookstore.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CatagoryDAO;
import com.bookstore.entity.Catagory;

public class CatagoryServices  {

	
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private CatagoryDAO catagoryDAO;
	
	
	public CatagoryServices(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
		
		
		
		catagoryDAO = new CatagoryDAO();
	}
	
	public void listCatagory(String message) throws ServletException, IOException {
		List<Catagory> listCatagory = catagoryDAO.listAll();
		
		request.setAttribute("listCatagory", listCatagory);
		
		if(message != null) {
		request.setAttribute("message", message);
		}
		
		String listPage = "catagory_list.jsp";
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(listPage);
		
		dispatcher.forward(request, response);
		
	}
	
	public void listCatagory() throws ServletException, IOException{
		listCatagory(null);
	}
	
	
	
	public void createCatagory() throws ServletException, IOException {
		String name= request.getParameter("name");
		
		Catagory existsCatagory = catagoryDAO.findByName(name);
		
		if(existsCatagory != null) {
			
			String message = "Culd not create catagory. Catagory with name " + name  + " is already exists";
			request.setAttribute("message", message);
			

			RequestDispatcher dispatcher = request.getRequestDispatcher("message.jsp");
			
			dispatcher.forward(request, response);
			
		}else {
			
			Catagory catagory = new Catagory(name);
			
			catagoryDAO.create(catagory);
			String  message = "New Catagory Created Successfully";
			
			listCatagory(message);
			
		}
	}
	
	public void editCatagory() throws ServletException, IOException {
		int catagoryId = Integer.parseInt(request.getParameter("id"));
		
		Catagory catagory  = catagoryDAO.get(catagoryId);
		
		request.setAttribute("catagory", catagory);
		

		RequestDispatcher dispatcher = request.getRequestDispatcher("catagory_form.jsp");
		
		dispatcher.forward(request, response);
		
	}

	public void updateCatagory() throws ServletException, IOException {
		
		int catagoryId = Integer.parseInt(request.getParameter("catagoryId"));
		
		String catagoryName = request.getParameter("name");
		
		Catagory catagoryById = catagoryDAO.get(catagoryId);
		
		Catagory catagoryByName = catagoryDAO.findByName(catagoryName);
		
		if(catagoryByName != null && catagoryById.getCatagoryId() != catagoryByName.getCatagoryId()) {
			
			String message = "Could not Update Catagory." + catagoryName + " is already exists.";
			
			request.setAttribute("message", message);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("message.jsp");
			
			dispatcher.forward(request, response);
 
		}else {
			
			catagoryById.setName(catagoryName);
			catagoryDAO.update(catagoryById);
			
			String message = "Catagory Has been Updeated Successfully";
			
			listCatagory(message);
		}
	}

	public void deleteCatagory() throws ServletException, IOException {
		
		int catagoryId = Integer.parseInt(request.getParameter("id"));
		
		BookDAO bookDAO = new BookDAO();
		
		long noOfBooks = bookDAO.countByCatagory(catagoryId);
		
		String message ;
		
		if(noOfBooks > 0) {
			
			message = "Could not delete the catagory (ID: %d) because it currently contains some books.";
			message = String.format(message, noOfBooks);
			
		}else {
			
			catagoryDAO.delete(catagoryId);
			
			message = "The catagory " + catagoryId + " has been removed successfully.";
			
		}
		
		
		
		listCatagory(message);
		
		
	}
	
	
}
