package codeu.controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

public class RegisterServletTest 
{

	private RegisterServlet registerServlet;
	private HttpServletRequest mockRequest;
	private PrintWriter mockPrintWriter;
	private HttpServletResponse mockResponse;

	@Before
	public void setup() throws IOException 
	{
		registerServlet = new RegisterServlet();
		mockRequest = Mockito.mock(HttpServletRequest.class);
		mockPrintWriter = Mockito.mock(PrintWriter.class);
		mockResponse = Mockito.mock(HttpServletResponse.class);
		Mockito.when(mockResponse.getWriter()).thenReturn(mockPrintWriter);
	}//setup

	@Test
	public void testDoGet() throws IOException, ServletException
	{
		registerServlet.doGet(mockRequest, mockResponse);
		Mockito.verify(mockPrintWriter).println("<h1>RegisterServlet GET request.</h1>");
	}//testDoGet
 
	@Test
	public void testDoPost_BadUsername() throws IOException, ServletException {
		Mockito.when(mockRequest.getParameter("username")).thenReturn("bad !@#$% username");

		loginServlet.doPost(mockRequest, mockResponse);

		Mockito.verify(mockRequest).setAttribute("error", "Please enter only letters, numbers, and spaces.");
		Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
	}//testDoPost_BadUsername

	@Test
	public void testDoPost_NewUser() throws IOException, ServletException
	{
		Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");

		UserStore mockUserStore = Mockito.mock(UserStore.class);
		Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);
		loginServlet.setUserStore(mockUserStore);

		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

		loginServlet.doPost(mockRequest, mockResponse);

		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

		Mockito.verify(mockUserStore).addUser(userArgumentCaptor.capture());
		Assert.assertEquals(userArgumentCaptor.getValue().getName(), "test username");

		Mockito.verify(mockSession).setAttribute("user", "test username");
		Mockito.verify(mockResponse).sendRedirect("/conversations");
 }//testDoPost_NewUser

	@Test
	public void testDoPost_ExistingUser() throws IOException, ServletException 
	{
		Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");

		UserStore mockUserStore = Mockito.mock(UserStore.class);
   		Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(true);
   		loginServlet.setUserStore(mockUserStore);

   		HttpSession mockSession = Mockito.mock(HttpSession.class);
   		Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

   		loginServlet.doPost(mockRequest, mockResponse);

   		Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));

   		Mockito.verify(mockSession).setAttribute("user", "test username");
   		Mockito.verify(mockResponse).sendRedirect("/conversations");
	}//testDoPost_ExistingUser
}//RegisterServletTest
