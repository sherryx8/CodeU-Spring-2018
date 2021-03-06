<%@ page import="java.util.List" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Collections" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%
String userName = (String) request.getAttribute("username");
String aboutMe = (String) request.getAttribute("aboutme");
List<Message> userMessages = (List<Message>) request.getAttribute("messages");
Collections.reverse(userMessages);
%>

<!DOCTYPE html>
<html>
<head>
	<title>Profile Page</title>
	<link rel="stylesheet" href="/css/main.css" type"text/css">

	<style>
		#chat {
			background-color: white;
			height: 500px;
			overflow-y: scroll
		}
	</style>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if (request.getSession().getAttribute("user") != null) { %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <% if (request.getSession().getAttribute("user") != null) { %>
      <a href="/logout">Logout</a>
    <% } %>
  </nav>

	<div id="container">

		<h1><%= userName %>'s Profile Page</h1>
		<% String name = request.getSession().getAttribute("user") + ""; %>
		<% if (!(name.equals(userName))) { %>
		<form action = "/conversations" method = "post">
			<button type="submit" name="message" value="<%=userName%>">Message</button>
		</form>
		<% } %>

		<hr/>

		<h2>About <%= userName %></h2>
		<p><%= aboutMe %></p>
		<br/>
		<% if (name.equals(userName)) { %>
		<h3>Edit your About Me (only you can see this)</h3>
		<form action="/users/<%= name %>" method="POST">
			<textarea rows="4" cols="115" name="aboutme"> </textarea>
			<br/>
			<button type="submit">Submit</button>
		</form>
		<% } %>
		<hr/>

		<h2><%= userName %>'s Sent Messages</h2>
		<div id="chat">
			<ul>
		<%
			for (Message message: userMessages) {
				Instant creationTime = message.getCreationTime();
				Date creationDate = Date.from(creationTime);
				String content = message.getContent();
		%>
			<li><strong><%= creationDate %>:</strong> <%= content %></li>
		<%
			}
		%>
			</ul>
		</div>
		<hr/>
		<br/><br/><br/>
	</div>
</body>
</html>
