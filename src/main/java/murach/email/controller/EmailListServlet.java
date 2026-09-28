package murach.email.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.email.dao.UserDAO;
import murach.email.model.User;
import murach.email.util.MailUtilGmail;

import java.io.IOException;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "add"; // default action cho form post
        }

        String url = "/index.jsp";
        if (action.equals("join")) {
            url = "/index.jsp";
        } else if (action.equals("add")) {
            // get parameters from the request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            User user = new User(firstName, lastName, email);
            String message;

            // Kiểm tra trùng lặp email bằng UserDAO (JPA)
            if (UserDAO.emailExists(email)) {
                message = "This email address already exists.<br>Please enter another email address.";
                url = "/index.jsp";
            } else {
                message = "";
                UserDAO.insert(user);

                // send email to user
                String to = email;
                String from = "minhgiauad2024@gmail.com";
                String subject = "Welcome to our email list";
                String body = "Dear " + firstName + ",\n\n"
                        + "Thanks for joining our email list. "
                        + "We'll make sure to send "
                        + "you announcements about new products "
                        + "and promotions.\n"
                        + "Have a great day and thanks again!\n\n"
                        + "Kelly Slivkoff\n"
                        + "Mike Murach & Associates";
                boolean isBodyHTML = false;

                try {
                    MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
                } catch (MessagingException e) {
                    String errorMessage = "ERROR: Unable to send email. "
                            + "Check Tomcat logs for details.<br>"
                            + "NOTE: You may need to configure your system "
                            + "as described in chapter 14.<br>"
                            + "ERROR MESSAGE: " + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    this.log("Unable to send email. \n"
                            + "Here is the email you tried to send: \n"
                            + "=====================================\n"
                            + "TO: " + email + "\n"
                            + "FROM: " + from + "\n"
                            + "SUBJECT: " + subject + "\n\n"
                            + body + "\n\n");
                }
                url = "/thanks.jsp";
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }
        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}