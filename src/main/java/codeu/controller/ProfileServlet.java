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
    String aboutMe = "Nothing to see here.";

    User user = userStore.getUser(userName);
    if (user.getAboutMe() != null){
      aboutMe = user.getAboutMe();
    }
    UUID userId = user.getId();
    List<Message> userMessages = messageStore.getMessagesForUser(userId);

    request.setAttribute("username", userName);
    request.setAttribute("messages", userMessages);
    request.setAttribute("aboutme", aboutMe);
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String aboutMe = request.getParameter("aboutme");
    String username = (String) request.getSession().getAttribute("user");
    User user = userStore.getUser(username);
    user.setAboutMe(aboutMe);
    userStore.updateUser(user, username, aboutMe);

    response.sendRedirect("/users/" + username);
    return;
  }

}
