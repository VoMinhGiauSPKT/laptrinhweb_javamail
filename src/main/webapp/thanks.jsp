<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Thanks for joining</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    </head>

    <body>

        <h1>Thanks for joining our email list</h1>
        <p>Here is the information that you entered:</p>

        <div class="form-row">
            <label>Email:</label>
            <span>${user.email}</span>
        </div>

        <div class="form-row">
            <label>First Name:</label>
            <span>${user.firstName}</span>
        </div>

        <div class="form-row">
            <label>Last Name:</label>
            <span>${user.lastName}</span>
        </div>

        <p><span class="message">${errorMessage}</span></p>

        <p>To enter another email address, click on the Return button below.</p>

        <!-- Nút Return quay về trang nhập Email -->
        <a href="emailList" class="btn">Return</a>

    </body>

    </html>