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
					if(typeof ui.newPanel[0] != "undefined"
							&& typeof ui.newPanel[0].dataset.msgids != "undefined") {
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
			
			// Sumbit of msg
			$(".btn-send-msg").click(function() {
				if(typeof this.dataset.userreceiverid != "undefined"
						&& typeof this.dataset.srid != "undefined") {
					var textareaId = "textarea-msg-" + this.dataset.srid;
					
					var payload = {
						userCallingId: webappDataObj.userData.id,
						userMsgReceiverId: "",
						srId: "",
						msgText: $("#" + textareaId).val()
					}
					payload.userMsgReceiverId = this.dataset.userreceiverid;
					payload.srId = this.dataset.srid;
					createRest("POST", "sendmessageservlet", JSON.stringify(payload), function(responseText) {
						$("#" + textareaId).val("");
						$.notify("Nachricht erfolgreich gesendet", "success");
					});
				} else {
					$.notify("Fehlgeschlagen Nachricht zu senden!", "error");
				}
			});
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
    						int unseenMessages = aManager.countUnreadMsgsInList(user, messageListOneSr);
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
		    										<% if(! msgTitle.equals("")){ %> <span class="message-title"><% out.write("- " + msgTitle); %></span><%}%>
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
			    									<% if(! msgTitle.equals("")){ %> <span class="message-title"><% out.write("- " + msgTitle); %></span><%}%>
		    										<span class="message-time-and-date"><% out.write(msgCreationDateTimeString); %></span>
		    										<span class="message-author"><% out.write(" von " + "<a href='user.jsp?id=" + msgAuthorId +  "'>" + msgAuthor + "</a>"); %></span>
		    										<p><% out.write(msg.getText()); %></p>
		    									</div>
    										</div>
    									<%
    								}
    							}
    						
    							// Enable chat-row
    							if(sr.getUserStasisfier() != null && sr.getStatus() == SearchRequestStatus.IN_PROGRESS) {
    								String msgUserReceiverId = "";
    								if(sr.getUserStasisfier().getId().toString().equals(user.getId().toString())) {
    									msgUserReceiverId = sr.getUserCreator().getId().toString();
    								} else {
    									msgUserReceiverId = sr.getUserStasisfier().getId().toString();
    								}
    								
    								%>
    									<div class="row send-msg-container">
    										<div class="col-md-10 col-xs-12">
    											<textarea id="textarea-msg-<% out.write(sr.getId().toString()); %>" class="full-width" rows="2"></textarea>
    										</div>
    										
    										<div class="col-md-2 col-xs-12">
    											<input type="button" data-userreceiverid="<% out.write(msgUserReceiverId); %>" data-srid="<% out.write(sr.getId().toString()); %>"
    												class="btn bt-default full-width btn-send-msg" value="Senden"/>
    										</div>
    									</div>
    								<%
    							}
    						%>
    							
    						</div>
    					<%}
    					
    					// Display welcome-msg if there arent messsages
    					if(relevantMsgs.size() == 0) {
    						%>
    							<h3 class="message-chatheader">Willkommen</h3>
    							<div class="message-chatcontainer">
    								<p>Wilkommen bei Folivora!</p>
    								<p>TODO Erklärung</p>
    								<p>TODO Links Kontakt & Handbuch</p>
    							</div>
    						<%
    					}
    					%>
    			</div>
    		</div>
    		
    		<!-- <div class="col-md-2 right-side">
    			right
    		</div>-->
    	</div>
    </div>
</body>
</html>