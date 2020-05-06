package no.kristiania.exam.tsdes.frontend;

import no.kristiania.exam.tsdes.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@Controller
public class SignUpController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;


    private String username;

    private String password;

    private String name;

    private String lastName;

    private String email;


    public String signUpUser(){

        boolean registered = false;
        try {
            registered = userService.createUser(username, name,lastName,password,email,"user");
        }catch (Exception e){
            //nothing to do
        }

        if(registered){

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    password,
                    userDetails.getAuthorities());

            authenticationManager.authenticate(token);

            if (token.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(token);
            }

            return "/index.jsf?faces-redirect=true";
        } else {
            return "/signup.jsf?faces-redirect=true&error=true";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
