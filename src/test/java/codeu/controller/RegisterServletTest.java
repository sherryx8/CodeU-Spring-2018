package codeu.controller;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

public class RegisterServletTest {
  private RegisterServlet registerServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  @Before
  public void setup() throws IOException {
    registerServlet = new RegisterServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/register.jsp"))
        .thenReturn(mockRequestDispatcher);
  } //setup

  @Test
  public void testDoGet() throws IOException, ServletException {
    registerServlet.doGet(mockRequest, mockResponse);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  } //testDoGet
  
  @Test
  public void testDoPost_UserNoMatch() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("Username Invalid");
    Mockito.when(mockRequest.getAttribute("user")).thenReturn("test_username");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    User Invalid_Username = new User (UUID.randomUUID(), "test_username", "password", Instant.now(), "About_Me_Text");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(Invalid_Username);
    
    RegisterServlet mockRegisterServlet = Mockito.mock(RegisterServlet.class);
    registerServlet.setUserStore(mockUserStore);
    
    Mockito.verify(mockRegisterServlet, Mockito.never());
    Mockito.verify(mockRequest).setAttribute("error", "Please enter only letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  } //testDoPost_UserNoMatch
  
  @Test
  public void testDoPost_ExistingUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("testing username...");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testing password...");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("testing username...")).thenReturn(true);
    registerServlet.setUserStore(mockUserStore);
  } //testDoPost_ExistingUser

  @Test
  public void testDoPost_NewUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("testing username....");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testing password....");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("testing username....")).thenReturn(false);
    registerServlet.setUserStore(mockUserStore);
  } //testDoPost_NewUser
} //RegisterServletTest
