package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;
import java.util.UUID;
import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the profile page. */
public class ProfileServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Set up state for handling user requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
   void setMessageStore(MessageStore messageStore) {
     this.messageStore = messageStore;
   }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String requestUrl = request.getRequestURI();
    String userName = requestUrl.substring("/users/".length());
    //TODO: Change to acutal about me, and handle null condition. Thar Min Htet
    String aboutMe = "This is fake about Me.";

    User user = userStore.getUser(userName);
    UUID userId = user.getId();
    List<Message> userMessages = messageStore.getMessagesForUser(userId);

    request.setAttribute("username", userName);
    request.setAttribute("messages", userMessages);
    request.setAttribute("aboutme", aboutMe);
    // TODO: Add About me here. Thar
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }
}
