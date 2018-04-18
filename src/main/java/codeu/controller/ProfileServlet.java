package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the profile page. */
public class ProfileServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  // private UserStore userStore;

  /** Set up state for handling user requests. */
  // @Override
  // public void init() throws ServletException {
  //   super.init();
  //   setUserStore(UserStore.getInstance());
  // }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  // void setUserStore(UserStore userStore) {
  //   this.userStore = userStore;
  // }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);

    // String username = (String) request.getSession().getAttribute("user"); //Dummny User
    // // User user = userStore.getUser(username);
    // User user = new User(null, "Ada", null, null);
    // request.setAttribute("user", user);
    // // Forward request to profile.jsp file
    // request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }
}
