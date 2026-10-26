
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.app.model.User" %>
<%@ include file="/includes/navbar.jsp" %>

<%
    User profileUser = (User) request.getAttribute("profileUser");

    if (profileUser == null) {
        response.sendRedirect(request.getContextPath() + "/profile");
        return;
    }

    String message = (String) request.getAttribute("profileMessage");
    String messageType =
        (String) request.getAttribute("profileMessageType");

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | BiteBox</title>
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/profile.css">
          <link rel="stylesheet"
          href="<%= contextPath %>/CSS/common.css">
          <link rel="stylesheet"
          href="<%= contextPath %>/CSS/navbar.css">
          <link rel="stylesheet"
          href="<%= contextPath %>/CSS/footer.css">
</head>
<body>

<main class="profile-page">
    <section class="profile-card">

        <div class="profile-heading">
            <div class="avatar">
                <%= profileUser.getName() != null
                    && !profileUser.getName().trim().isEmpty()
                    ? profileUser.getName().trim().substring(0, 1).toUpperCase()
                    : "U" %>
            </div>

            <div>
                <h1>My Profile</h1>
                <p>Manage your personal information</p>
            </div>
        </div>

        <form id="profileForm"
              action="<%= contextPath %>/profile"
              method="post">

            <div class="profile-fields">

                <div class="profile-field">
                    <label for="name">Full Name</label>
                    <div class="field-control">
                        <input type="text" id="name" name="name"
                               value="<%= escapeHtml(profileUser.getName()) %>"
                               maxlength="100" required readonly>
                        <button type="button" class="edit-button"
                                aria-label="Edit full name"
                                aria-controls="name"
                                onclick="enableEdit('name', this)">
                            &#9998;
                        </button>
                    </div>
                </div>

                <div class="profile-field">
                    <label for="username">Username</label>
                    <div class="field-control">
                        <input type="text" id="username" name="username"
                               value="<%= escapeHtml(profileUser.getUsername()) %>"
                               maxlength="100" required readonly>
                        <button type="button" class="edit-button"
                                aria-label="Edit username"
                                aria-controls="username"
                                onclick="enableEdit('username', this)">
                            &#9998;
                        </button>
                    </div>
                </div>

                <div class="profile-field">
                    <label for="email">Email Address</label>
                    <div class="field-control">
                        <input type="email" id="email" name="email"
                               value="<%= escapeHtml(profileUser.getEmail()) %>"
                               maxlength="254" required readonly>
                        <button type="button" class="edit-button"
                                aria-label="Edit email"
                                aria-controls="email"
                                onclick="enableEdit('email', this)">
                            &#9998;
                        </button>
                    </div>
                </div>

                <div class="profile-field">
                    <label for="phoneNumber">Phone Number</label>
                    <div class="field-control">
                        <input type="tel" id="phoneNumber" name="phoneNumber"
                               value="<%= escapeHtml(profileUser.getPhoneNumber()) %>"
                               maxlength="15" required readonly>
                        <button type="button" class="edit-button"
                                aria-label="Edit phone number"
                                aria-controls="phoneNumber"
                                onclick="enableEdit('phoneNumber', this)">
                            &#9998;
                        </button>
                    </div>
                </div>

                <div class="profile-field full-width">
                    <label for="address">Address</label>
                    <div class="field-control">
                        <textarea id="address" name="address"
                                  maxlength="500" required readonly><%= escapeHtml(profileUser.getAddress()) %></textarea>
                        <button type="button" class="edit-button"
                                aria-label="Edit address"
                                aria-controls="address"
                                onclick="enableEdit('address', this)">
                            &#9998;
                        </button>
                    </div>
                </div>

            </div>

            <div class="profile-actions">
                <button type="reset" class="cancel-button"
                        onclick="resetProfileFields()">
                    Reset Changes
                </button>

                <button type="submit" class="update-button">
                    Update Details
                </button>
            </div>

        </form>
    </section>
</main>

<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<% if (message != null && !message.trim().isEmpty()) { %>
<div class="popup-overlay" id="messagePopup"
     role="presentation">
    <div class="popup-card" role="alertdialog"
         aria-modal="true" aria-labelledby="popupTitle">

        <div class="popup-icon <%= "success".equals(messageType)
            ? "success-icon" : "error-icon" %>">
            <%= "success".equals(messageType) ? "&#10003;" : "!" %>
        </div>

        <h2 id="popupTitle">
            <%= "success".equals(messageType)
                ? "Success" : "Notice" %>
        </h2>

        <p><%= escapeHtml(message) %></p>

        <button type="button" class="popup-button"
                onclick="closePopup()">Continue</button>
    </div>
</div>
<% } %>

<script>
    const originalValues = {};

    document.querySelectorAll(
        "#profileForm input, #profileForm textarea"
    ).forEach(function (field) {
        originalValues[field.id] = field.value;
    });

    function enableEdit(fieldId, button) {
        const field = document.getElementById(fieldId);

        field.readOnly = false;
        field.classList.add("field-editing");
        button.classList.add("edit-active");
        button.setAttribute("aria-label", "Editing " + fieldId);
        field.focus();

        if (field.tagName === "INPUT") {
            field.select();
        }
    }

    function resetProfileFields() {
        document.querySelectorAll(
            "#profileForm input, #profileForm textarea"
        ).forEach(function (field) {
            field.value = originalValues[field.id];
            field.readOnly = true;
            field.classList.remove("field-editing");
        });

        document.querySelectorAll(".edit-button").forEach(function (button) {
            button.classList.remove("edit-active");
        });
    }

    function closePopup() {
        const popup = document.getElementById("messagePopup");
        if (popup) {
            popup.remove();
        }
    }

    document.querySelectorAll(".edit-button").forEach(function (button) {
        button.addEventListener("click", function () {
            button.textContent = "\u2713";
        });
    });

    document.querySelectorAll(".edit-button").forEach(function (button) {
        button.addEventListener("click", function () {
            button.classList.add("edit-active");
        });
    });
</script>

<%@ include file="/includes/footer.jsp" %>

</body>
</html>
