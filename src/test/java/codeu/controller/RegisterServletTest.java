package codeu.controller;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mindrot.jbcrypt.*;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

public class RegisterServletTest
{
  private RegisterServlet registerServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
 
  @Before
  public void setup() throws IOException
  {
    registerServlet = new RegisterServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/register.jsp")).thenReturn(mockRequestDispatcher);
  }//setup

  @Test
  public void testDoGet() throws IOException, ServletException
  {
    registerServlet.doGet(mockRequest, mockResponse);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }//testDoGet

  @Test // This tests that the password supplied to register is hashed properly
  public void testDoPost_hashedPassword() throws IOException, ServletException 
  {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("test password");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);
    registerServlet.setUserStore(mockUserStore);

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    registerServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    Mockito.verify(mockUserStore).addUser(userArgumentCaptor.capture());

    // checks that password is stored correctly
    Assert.assertNotEquals(userArgumentCaptor.getValue().getPassword(), "test password");
    Assert.assertTrue(BCrypt.checkpw("test password", userArgumentCaptor.getValue().getPassword()));

    Mockito.verify(mockResponse).sendRedirect("/login");
  }//testDoPost_hashedPassword

}//RegisterServletTest
