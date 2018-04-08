// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a
   * common setup method for use by the test framework or the servlet's init()
   * function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common
   * setup method for use by the test framework or the servlet's init()
   * function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common
   * setup method for use by the test framework or the servlet's init()
   * function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the
   * conversation title from the URL, finds the corresponding Conversation, and
   * fetches the messages in that Conversation. It then forwards to chat.jsp for
   * rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets
   * the logged-in username from the session, the conversation title from the
   * URL, and the chat message from the submitted form data. It creates a new
   * Message from that data, adds it to the model, and then redirects back to
   * the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }

    String messageContent = request.getParameter("message");

    // this removes any HTML from the message content
    String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

    String boldItalicsMessageContent = markDownToHTML(cleanedMessageContent);
    Message message = new Message(UUID.randomUUID(), conversation.getId(), user.getId(), boldItalicsMessageContent,
        Instant.now());

    messageStore.addMessage(message);

    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationTitle);
  }

  // identifier constants
  private final int MARKDOWN_BOLDITALIC_IDENTIFIER_LENGTH = "***".length();
  private final int MARKDOWN_BOLD_IDENTIFIER_LENGTH = "**".length();
  private final int MARKDOWN_ITALIC_IDENTIFIER_LENGTH = "*".length();
  private final int MARKDOWN_ESCAPE_IDENTIFIER_LENGTH = "\\*".length();
  private final int BBCODE_COLOR_BEGIN_IDENTIFIER_LENGTH = "\\[color=".length() - 1;
  private final int BBCODE_COLOR_END_IDENTIFIER_LENGTH = "\\[/color\\]".length() - 2;
  private final int BBCODE_SIZE_BEGIN_IDENTIFIER_LENGTH = "\\[size=".length() - 1;
  private final int BBCODE_SIZE_END_IDENTIFIER_LENGTH = "\\[/size\\]".length() - 2;

  /**
   * this function replaces a word surrounded by ** with a bold HTML tag
   */
  public String markDownBoldToHTML(String markdownChunk) {
    String content = markdownChunk.substring(MARKDOWN_BOLD_IDENTIFIER_LENGTH,
        markdownChunk.length() - MARKDOWN_BOLD_IDENTIFIER_LENGTH);
    return String.format("<b>%s</b>", content);
  }

  /**
   * this function replaces a word surrounded by * with an italics HTML tag
   */
  public String markDownItalicToHTML(String markdownChunk) {
    String content = markdownChunk.substring(MARKDOWN_ITALIC_IDENTIFIER_LENGTH,
        markdownChunk.length() - MARKDOWN_ITALIC_IDENTIFIER_LENGTH);
    return String.format("<i>%s</i>", content);
  }

  /**
   * this function replaces a word surrounded by *** with a bold-italics HTML
   * tag
   */
  public String markDownBoldItalicToHTML(String markdownChunk) {
    String content = markdownChunk.substring(MARKDOWN_BOLDITALIC_IDENTIFIER_LENGTH,
        markdownChunk.length() - MARKDOWN_BOLDITALIC_IDENTIFIER_LENGTH);
    return String.format("<b><i>%s</i></b>", content);
  }

  /**
   * this function replaces words surrounded with [color="<COLOR>"] ... [/color]
   * with a color HTML tag
   */
  public String bbCodeColorToHTML(String markdownChunk) {
    String content = markdownChunk.substring(BBCODE_COLOR_BEGIN_IDENTIFIER_LENGTH,
        markdownChunk.length() - BBCODE_COLOR_END_IDENTIFIER_LENGTH);
    String[] colorContent = content.split("\\]");
    String color = colorContent[0];
    content = colorContent[1];
    return String.format("<font color=\"%s\">%s</font>", color, content);
  }

  /**
   * this function replaces words surrounded with [size="<SIZE>"] ... [/size]
   * with a font size HTML tag
   */
  public String bbCodeSizeToHTML(String markdownChunk) {
    String content = markdownChunk.substring(BBCODE_SIZE_BEGIN_IDENTIFIER_LENGTH,
        markdownChunk.length() - BBCODE_SIZE_END_IDENTIFIER_LENGTH);
    String[] sizeContent = content.split("\\]");
    String size = sizeContent[0];
    content = sizeContent[1];
    return String.format("<font size=\"%s\">%s</font>", size, content);
  }

  /**
   * this function replaces *, **, and *** identifiers with the appropriate HTML
   * tag
   */
  public String markDownToHTML(String message) {
    // bold-italic html converter ***
    Pattern patternBoldItalic = Pattern.compile("(?<!\\\\)([*_]{2})([*_])([^\n]+?)([*_]{2})([*_])");
    Matcher matcherBoldItalic = patternBoldItalic.matcher(message);
    String chunk;
    while (matcherBoldItalic.find()) {
      chunk = matcherBoldItalic.group();
      message = message.replace(chunk, markDownBoldItalicToHTML(chunk));
    }

    // bold html converter **
    Pattern patternBold = Pattern.compile("(?<!\\\\)([*]{2})([^\n]+?)([*]{2})");
    Matcher matcherBold = patternBold.matcher(message);
    while (matcherBold.find()) {
      chunk = matcherBold.group();
      message = message.replace(chunk, markDownBoldToHTML(chunk));
    }

    // italic html converter *
    Pattern patternItalic = Pattern.compile("(?<!\\\\)([*]{1})([^\n]+?)([*]{1})");
    Matcher matcherItalic = patternItalic.matcher(message);
    while (matcherItalic.find()) {
      chunk = matcherItalic.group();
      message = message.replace(chunk, markDownItalicToHTML(chunk));
    }

    // escape character html converter
    Pattern patternEscape = Pattern.compile("(\\\\\\*)");
    Matcher matcherEscape = patternEscape.matcher(message);
    while (matcherEscape.find()) {
      chunk = matcherEscape.group();
      message = message.replace(chunk, "*");
    }

    // color html converter
    Pattern patternColor = Pattern.compile("(?<!\\\\)(\\[color=\\w+\\])([^\\n]+?)(\\[\\/color\\])");
    Matcher matcherColor = patternColor.matcher(message);
    while (matcherColor.find()) {
      chunk = matcherColor.group();
      message = message.replace(chunk, bbCodeColorToHTML(chunk));
    }

    // size html converter
    Pattern patternSize = Pattern.compile("(?<!\\\\)(\\[size=\\w+\\])([^\\n]+?)(\\[\\/size\\])");
    Matcher matcherSize = patternSize.matcher(message);
    while (matcherSize.find()) {
      chunk = matcherSize.group();
      message = message.replace(chunk, bbCodeSizeToHTML(chunk));
    }

    // escape character html converter \[
    Pattern patternEscapeBracket = Pattern.compile("\\\\\\[");
    Matcher matcherEscapeBracket = patternEscapeBracket.matcher(message);
    while (matcherEscapeBracket.find()) {
      chunk = matcherEscapeBracket.group();
      System.out.println("The chunk is: " + chunk);
      message = message.replace(chunk, "[");
    }
    return message;
  }
}
