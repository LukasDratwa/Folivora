<%@page import="java.util.*"%>
<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.*"%>
<%@page import="de.folivora.model.messenger.*"%>
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
					// console.log("after" , ui);
					if(typeof ui.newPanel[0] != "undefined") {
						var payload = {
							userCallingId: webappDataObj.userData.id,
							msgIds: ui.newPanel[0].dataset.msgids
						}
						createRest("POST", "setmessageseenservlet", JSON.stringify(payload), null);
					}
					
					if(typeof ui.newHeader[0] != "undefined") {
						$(ui.newHeader[0]).find(".message-amount-unread-msgs-number").html(0);
					}
				},
				beforeActivate: function(event, ui) {
					// console.log("before " , ui);
					// TODO scroll down in display
				}
			});
			$("#message-accordion").removeClass("hidden");
			$(".message-chatcontainer").css("max-height", $(window).height() * 0.5);
			
			
			// Mobile view adaption
			if(screen.width < 550) {
				$(".message-amount-unread-msgs-number").each(function(i) {
					var o = $(".message-amount-unread-msgs-number")[i];
					var spanHtml = $(o).wrap('<p/>').parent().html();
					$(o).unwrap();
					$(o.parentElement).html(spanHtml);
				});
			}
		});
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
    							<span class="message-amount-unread-msgs">
    								<span class="message-amount-unread-msgs-number" id="message-amount-unread-msgs-<% out.write(sr.getId().toString()); %>">
    									<% 	if(unseenMessages > 0){ out.write("<b class='red'>"); };
    											out.write("" + unseenMessages);
    										if(unseenMessages > 0){ out.write("</b>"); };
    									%>
    								</span>
    								
    								<% out.write("Ungelesene Nachrichten:"); %>
    							</span>
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
    						
    							// Enable chat-row
    							if(sr.getUserStasisfier() != null) {
    								%>
    									<div class="row">
    										<div class="col-md-10 col-xs-12">
    											<textarea class="full-width" rows="2"></textarea>
    										</div>
    										
    										<div class="col-md-2 col-xs-12">
    											<input type="button" class="btn bt-default full-width" value="Senden"/>
    										</div>
    									</div>
    								<%
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