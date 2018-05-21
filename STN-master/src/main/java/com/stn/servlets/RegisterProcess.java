package com.stn.servlets;

import com.stn.helpers.ApplicationHelper;
import com.stn.helpers.UserHelper;
import com.stn.helpers.SecurityHelper;
import com.stn.pojo.Aplicatie;
import com.stn.pojo.Invitatie;
import com.stn.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@WebServlet("/RegisterProcess")
public class RegisterProcess extends HttpServlet {

    private void setTempFields(HttpServletRequest request, String user, String firstName, String lastName, String email) {
       HttpSession session= request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("firstname", firstName);
        session.setAttribute("lastname", lastName);
        session.setAttribute("email", email);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String error ="";
        String url = "index.jsp";
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //Setam toti parametrii pe care ii primim din form
        String username = request.getParameter("user");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String terms = request.getParameter("terms");
        String faq= request.getParameter("faq");
        String inviteCode = request.getParameter("invitecode");

        ApplicationHelper applicationHelper = new ApplicationHelper();
        Invitatie invitatie = null;

        if(inviteCode != null || !inviteCode.equals("")) {
            try {
              invitatie = applicationHelper.getInvitatie(inviteCode);
            } catch (SQLException | ClassNotFoundException e) {
                out.println(e);
                return;
            }
        }

        String hashedPassword = "";
        byte[] salt;
        String extra = "";

        if(!Validator.isEmpty(inviteCode)) {
            extra = "?invite=" + inviteCode;
        }

        if(invitatie == null && !Validator.isEmpty(inviteCode)) {
            error = "Invalid invite code!";
            url = "register.jsp" + extra;
        } else if(Validator.isEmpty(username, password1, password2, email, firstName, lastName)) {
            error = "You must fill all the requiered fields!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.between(username, 3, 20)) {
            error = "Username must be between 3 and 20 characters!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.between(firstName,2,30)) {
            error = "Firstname must be between 2 and 30 characters!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.isName(firstName)) {
            error = "Invalid Firstname!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.between(lastName,2,30)) {
            error = "Lastname must be between 2 and 30 characters!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.isName(lastName)) {
            error = "Invalid Lastname!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.between(email,6,30)) {
            error = "Email must be between 6 and 30 characters!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!Validator.isEmail(email)) { //verificare email daca este valid
            error = "Invalid email addres!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(!password1.equals(password2)) {
            error = "The passwords are not matching!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(password1.length() < 6) {
            error = "Password must have at least 6 characters!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        }
        else if(terms == null || faq == null ) { // verificare daca au fost bifate casutele cu terms si faq
            error = "You must agree to the conditions!";
            url = "register.jsp" + extra;
            this.setTempFields(request,username,firstName,lastName,email);
        } else {
            //Verificare daca username-ul sau email-ul se gaseste deja in baza de date

            UserHelper userHelper = new UserHelper();

            try {
                if(userHelper.checkAvailability(username, email)) { //daca nu avem rezultate,inseamna ca user-ul sau parola nu exista in baza de date
                   //Introducere utilizator in baza de date

                    SecurityHelper securityHelper = new SecurityHelper();

                    securityHelper.generateSalt();
                    salt = securityHelper.getSalt();
                    hashedPassword = securityHelper.getHash(password1);

                    if(invitatie != null) {
                        userHelper.addUser(username,hashedPassword,salt,email,firstName,lastName,invitatie.getIdGrupa(),invitatie.getIdSerie(),invitatie.getIdFacultate(),invitatie.getUserClass());
                        applicationHelper.deleteInvitatie(invitatie.getIdInvitatie());
                    } else {
                        userHelper.addUser(username,hashedPassword,salt,email,firstName,lastName,0,0,0,1);
                    }

                }
                else
                {
                    error = "Username or email already in use!";
                    url = "register.jsp" + extra;
                    this.setTempFields(request,username,firstName,lastName,email);
                }
            } catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException e) {
                out.println(e);
                return;
            }
        }

        //Redirectionare catre o anumita pagina (este data de string-ul url)
        session.setAttribute("error", error);
        response.sendRedirect(url);
    }
}
