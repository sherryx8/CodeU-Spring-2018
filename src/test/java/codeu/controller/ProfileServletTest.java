package codeu.controller;

import java.io.IOException;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;
  private MessageStore mockMessageStore;

  @Before
  public void setup() throws IOException {
    profileServlet = new ProfileServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
      .thenReturn(mockRequestDispatcher);

    mockMessageStore = Mockito.mock(MessageStore.class);
    profileServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    profileServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException{
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/test_user");

    UUID fakeUserId = UUID.randomUUID();
    User fakeUser = new User(fakeUserId, "test_user", "password", Instant.now());
    Mockito.when(mockUserStore.getUser("test_user"))
      .thenReturn(fakeUser);

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            UUID.randomUUID(),
            fakeUserId,
            "test message",
            Instant.now()));
    Mockito.when(mockMessageStore.getMessagesForUser(fakeUserId))
      .thenReturn(fakeMessageList);


    profileServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("username", "test_user");
    Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
