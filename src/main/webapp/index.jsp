<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

    <h1>Join our email list</h1>
    <p>To join our email list, enter your name and email address below.</p>

    <!-- Thông báo lỗi nếu trùng Email -->
    <div class="message">${message}</div>

    <form action="emailList" method="post">
        <div class="form-row">
            <label>Email:</label>
            <input type="email" name="email" value="${user.email}" required>
        </div>

        <div class="form-row">
            <label>First Name:</label>
            <input type="text" name="firstName" value="${user.firstName}" required>
        </div>

        <div class="form-row">
            <label>Last Name:</label>
            <input type="text" name="lastName" value="${user.lastName}" required>
        </div>

        <div class="btn-container">
            <input type="submit" value="Join Now">
        </div>
    </form>

</body>
</html>