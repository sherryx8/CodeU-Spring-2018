package codeu.controller;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet class responsible for user registration.
*/
<<<<<<< HEAD
public class RegisterServlet extends HttpServlet 
{
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException 
	{

		response.getWriter().println("<h1>RegisterServlet GET request.</h1>");
		request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
	}//doGet
 
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException 
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//Username Error Handling
		if (!username.matches("[\\w*\\s*]*")) 
		{
		     request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
		     request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
		     return;
		}//if

		 //Detect whether a username is taken & to register new users
		 if (userStore.isUserRegistered(username)) 
		 {
		     request.setAttribute("error", "That username is already taken.");
		     request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
		     return;
		 }//if

		 User user = new User(UUID.randomUUID(), username, password, Instant.now());
		 userStore.addUser(user);

		 response.sendRedirect("/login");
		   
		 response.getWriter().println("<p>Username: " + username + "</p>");
		 response.getWriter().println("<p>Password: " + password + "</p>");
	}//doPost

	/**
	 * Store class that gives access to Users.
	 */
	private UserStore userStore;

	/**
	 * Set up state for handling registration-related requests. This method is only called when
	 * running in a server, not when running in a test.
	 */
	@Override
	public void init() throws ServletException 
	{
		super.init();
		setUserStore(UserStore.getInstance());
	}//init

	/**
	 * Sets the UserStore used by this servlet. This function provides a common setup method
	 * for use by the test framework or the servlet's init() function.
	 */
	void setUserStore(UserStore userStore)
	{
		this.userStore = userStore;
	}//setUserStore
=======
public class RegisterServlet extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
  { 
    response.getWriter().println("<h1>RegisterServlet GET request.</h1>");
  }//doGet
>>>>>>> origin/master
}//RegisterServlet

