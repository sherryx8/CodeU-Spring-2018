<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>Error 404</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <% if (request.getSession().getAttribute("user") != null) { %>
      <a href="/logout">Logout</a>
    <% } %>
  </nav>

  <div id="container">
    <h1 align="center" style="font-size:75px;"> 404 </h1>
    <h3 align="center"> Grats! You broke it, bro. The user you're looking for doesn't exist. </h3>
    <iframe align="middle" style="margin-left:160px" src="https://giphy.com/embed/l41lFw057lAJQMwg0" width="480" height="270" frameBorder="0" class="giphy-embed"></iframe><p></p>
  </div>
</body>
</html>
