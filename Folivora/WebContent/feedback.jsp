<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.User"%>
<%@page import="de.folivora.model.Feedback"%>
<%@page import="de.folivora.model.SearchRequest"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="webappHead.jsp" %>

<%
	User userFeedbackCreator = (User)request.getAttribute("user");
	String srId = request.getParameter("srId");
	SearchRequest srFeedback = ApplicationManager.getApplicationManagerInstance().getSearchRequestWithId(srId);
	
	User userFeedbackReceiver = null;
	if(srFeedback.getUserCreator().getId().toString().equals(userFeedbackCreator.getId().toString())) {
		userFeedbackReceiver = srFeedback.getUserStasisfier();
	} else {
		userFeedbackReceiver = srFeedback.getUserCreator();
	}
	
	boolean userCreatedFeedbackAlready = false;
	Feedback alreadyCreatedFeedback = null;
	if(userFeedbackCreator.getId().toString().equals(srFeedback.getUserCreator().getId().toString())) {
		// Feedback creator == creator of sr
		if(srFeedback.getFeedbackOfSearchingUser() != null) {
			userCreatedFeedbackAlready = true;
			alreadyCreatedFeedback = srFeedback.getFeedbackOfSearchingUser();
		}
	} else {
		if(srFeedback.getFeedbackOfDeliveringUser() != null) {
			userCreatedFeedbackAlready = true;
			alreadyCreatedFeedback = srFeedback.getFeedbackOfDeliveringUser();
		}
	}
%>

<script>
	var feedbackRating = 0;
	$(document).ready(function() {
		$('#star-rating').rating(function(vote, event) {
			feedbackRating = vote;
		});
		
		
		
		<%
			if(userCreatedFeedbackAlready) {
				%>
					$('#star-rating-show-feedback').rating();
					setStars(<% out.write("" + alreadyCreatedFeedback.getRating().getVal()); %>)
				<%
			}
		%>
		
		$('#btn-submit-feedback').click(function() {
			if(feedbackRating == 0) {
				$.notify("Bitte mindestens einen Stern vergeben.", "info");
				return;
			}
			
			var payload = {
				srId: '<% out.write(srId); %>',
				userCallingId: webappDataObj.userData.id,
				stars: feedbackRating,
				feedbackText: $("#feedback-text").val()
			}
			console.log(payload);
			console.log(JSON.stringify(payload));
			createRest("POST", "newfeedbackservlet", JSON.stringify(payload), function(responseText) {
				if(lastRestStatus == 200) {
					$.notify("Feedback erfolgreich gesendet, vielen Dank!", "success");
					$('#btn-submit-feedback').prop('disabled', true);
				} else {
					$.notify("Feedback konnte nicht erstellt werden!", "error");
				}
			});
		});
	});
	
	function setStars(stars) {
		console.log(stars);
		
		var counter = 0;
		$(".star").each(function(i, elem) {
			if(counter < stars) {
				$(elem).addClass("fullStar");
				counter++;
			}
		});
	}
</script>

<body>
    <%@ include file="webappNavbar.jsp" %>
    <div class="container">
    	<div class="row">
    		<div class="col-md-12">
    			<h4>Feedback für <a href="user.jsp?id<% out.write(userFeedbackReceiver.getId().toString());%>"><% out.write(userFeedbackReceiver.getName()); %></a>
    			 zum Gesuch "<% out.write(srFeedback.getTitle()); %>"</h4>
    		</div>
    		<% if(!userCreatedFeedbackAlready) {%>
	    		<div class="col-md-12">
	    			<form role="form" id="feedback-form"  method="post">
						<div class="form-group">
							<label for=feedback-text>Feedback:</label>
							<input name="feedback-text" type="text" class="form-control" id="feedback-text"
								placeholder="Ein kurzes Feedback eingeben">
						</div>
						
						<label for=feedback-rating>Bewertung (1 sehr schlecht bis 5 sehr gut):</label>
						<div name="feedback-rating" id="star-rating">
						    <input type="radio" name="example" class="rating" value="1" />
						    <input type="radio" name="example" class="rating" value="2" />
						    <input type="radio" name="example" class="rating" value="3" />
						    <input type="radio" name="example" class="rating" value="4" />
						    <input type="radio" name="example" class="rating" value="5" />
						</div>
						
						<br>
						<input value="Feedback abgeben" class="btn btn-default" id="btn-submit-feedback">
					</form>
	    		</div>
	    	<% } else { %>
	    		<div class="col-md-12">
	    			<p>Folgendes Feedback wurde bereits dankend von Ihnen empfangen:</p>
	    			<p>Bewertung: <% out.write(alreadyCreatedFeedback.getRating().getAsString()); %></p>
	    			<div id="star-rating-show-feedback">
						<input type="radio" name="example" class="rating star-rating-show-feedback" value="1" />
						<input type="radio" name="example" class="rating star-rating-show-feedback" value="2" />
						<input type="radio" name="example" class="rating star-rating-show-feedback" value="3" />
						<input type="radio" name="example" class="rating star-rating-show-feedback" value="4" />
						<input type="radio" name="example" class="rating star-rating-show-feedback" value="5" />
					</div>
	    			<p>Feedback: "<% if(alreadyCreatedFeedback.getDescription() != null) out.write(alreadyCreatedFeedback.getDescription()); %>"</p>
	    		</div>
	    	<% } %>
    	</div>
    </div>
</body>


</html>