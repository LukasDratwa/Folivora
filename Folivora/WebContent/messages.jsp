<%@page import="java.util.*"%>
<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.*"%>
<%@page import="de.folivora.model.messanger.*"%>
<%@page import="de.folivora.util.*"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <%@ include file="webappHead.jsp" %>
  
  <body>
    <%@ include file="webappNavbar.jsp" %>

	<script>
		$(document).ready(function() {
			$("#message-accordion").accordion({
				heightStyle: "content",
				collapsible: true,
				active: false,
				activate: function(event, ui) {
					console.log("after" , ui);
					// TODO set the msgs unseen
				},
				beforeActivate: function(event, ui) {
					console.log("before " , ui);
					// TODO scroll down in display
				}
			});
			$("#message-accordion").removeClass("hidden");
			$(".message-chatcontainer").css("max-height", $(window).height() * 0.5);
		});
		
		// TODO implementieren eine Nachricht schreiben zu können
	</script>
    
    <div class="container">
    	<div class="row">
    		<div class="col-md-12 left-side full-height" id="message-list-container">
    			<div id="message-accordion" class="hidden">
    				<%
	    				User user = (User)request.getAttribute("user");
    					ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
    				
    					List<List<Message>> relevantMsgs = aManager.getRelevantMessagesOfUserForChatDisplay(user);
    					for(List<Message> messageListOneSr : relevantMsgs) {
    						int unseenMessages = aManager.countUnreadMsgsInList(messageListOneSr);
    						SearchRequest sr = messageListOneSr.get(0).getReferencedSr();
    						String srCreationDateTimeString = Util.formatDateToDateAndTimeString(sr.getCreationTimestamp());
    						
    						%><h3 class="message-chatheader">Gesuch "<% out.write(sr.getTitle()); %>" vom <% out.write(srCreationDateTimeString); %>
    							<span class="message-amount-unread-msgs"><% out.write("Ungelesene Nachrichten: " + unseenMessages); %></span>
    						</h3>
    						
    						<div class="message-chatcontainer" data-msgids="<%out.write(aManager.getMsgIdsOfMsgList(messageListOneSr));%>">
    						<%
    							for(Message msg : messageListOneSr) {
    								String msgCreationDateTimeString = Util.formatDateToDateAndTimeString(msg.getCreationTimestamp());
    								String msgAuthor = "";
    								String msgAuthorId = "";
    								String msgTitle = (msg.getTitle() == null) ? "" : msg.getTitle();
    								
    								if(msg.getReceiver().getId().toString().equals(user.getId().toString())) {
    									// From other user, display on left side
    									msgAuthor = msg.getReceiver().getName();
    									msgAuthorId = msg.getReceiver().getId().toString();
    									%>
    										<div class="row">
		    									<div class="col-md-8 col-xs-8">
		    										<span class="message-time-and-date"><% out.write(msgCreationDateTimeString); %></span>
		    										<span class="message-title"><% if(! msgTitle.equals("")) out.write("- " + msgTitle); %></span>
		    										<span class="message-author"><% out.write(" von " + "<a href='user.jsp?id=" + msgAuthorId +  "'>" + msgAuthor + "</a>"); %></span>
		    										<p><% out.write(msg.getText()); %></p>
		    									</div>
		    									
	    										<div class="col-md-4 col-xs-4"></div>
    										</div>
    									<%
    								} else {
    									// From the actual signed in user, display on the right
    									msgAuthor = user.getName();
    									msgAuthorId = user.getId().toString();
    									%>
    										<div class="row">
	    										<div class="col-md-4 col-xs-4"></div>
	    							
			    								<div style="direction: rtl;" class="col-md-8 col-xs-8">
		    										<span class="message-time-and-date"><% out.write(msgCreationDateTimeString); %></span>
		    										<span class="message-title"><% if(! msgTitle.equals("")) out.write("- " + msgTitle); %></span>
		    										<span class="message-author"><% out.write(" von " + "<a href='user.jsp?id=" + msgAuthorId +  "'>" + msgAuthor + "</a>"); %></span>
		    										<p><% out.write(msg.getText()); %></p>
		    									</div>
    										</div>
    									<%
    								}
    							}
    						%>
    						
    						</div>
    					<%}%>
    			</div>
    		</div>
    		
    		<!-- <div class="col-md-2 right-side">
    			right
    		</div>-->
    	</div>
    </div>
</body>
</html>