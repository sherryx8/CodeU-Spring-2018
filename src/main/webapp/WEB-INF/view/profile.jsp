<!DOCTYPE html>
<html>
<head>
	<title>Profile Pages</title>
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
  </nav>

	<div id="container">

		<h1>Ada's Profile Page</h1>
		<hr/>

		<h2>About Ada</h2>
		<p>Update your about me</p>
		<br/>

		<h3>Edit your About Me (only you can see this)</h3>
		<form>
			<textarea rows="4" cols="115"> </textarea>
			<br/>
			<input type="submit" value="Submit">
		</form>
		<hr/>

		<h2>Ada's Sent Messages</h2>
		<div id="chat">
			<ul>
				<li>Hello</li>
				<li>Hello</li>
				<li>Hello</li>
				<li>Hello</li>
			</ul>
		</div>
		<hr/>
		<br/><br/><br/>
	</div>
</body>
</html>
