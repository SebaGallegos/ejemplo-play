package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import play.i18n.MessagesApi;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class HomeController extends Controller{

    private final Form<UserProcess> form;
    private MessagesApi messagesApi;
    private final String dbPath = new File("app/models/database.db").getAbsolutePath();
    private final String jdbc = "jdbc:sqlite:" + dbPath;

    @Inject
    public HomeController(FormFactory formFactory, MessagesApi messagesApi){
        this.form = formFactory.form(UserProcess.class);
        this.messagesApi = messagesApi;
    }

    private void createDB() throws SQLException{
        File dbFile = new File(dbPath);
        if (!dbFile.exists()){
            try (Connection connection = DriverManager.getConnection(jdbc)){
                String createTable = "CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT), name TEXT, email TEXT)";
                try (Statement statement = connection.createStatement()){
                    statement.executeUpdate(createTable);
                }
            } catch (SQLException e){
                throw e;
            }
        }
    }

    public Result index(){
        return ok(views.html.index.render());
    }

    public Result listForm(Http.Request request){
        return ok(views.html.form.render(form, request, messagesApi.preferred(request)));
    }

    public Result submitForm(Http.Request request){
        final Form<UserProcess> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()){
            return badRequest();
        } else{
            UserProcess userProcess = boundForm.get();
            User user = new User(userProcess.getName(), userProcess.getEmail());

            return ok("Ingresado");
        }
    }

    public Result createUser(Http.Request request){
        try {
            createDB();
            String name = request.body().asFormUrlEncoded().get("name")[0];
            String email = request.body().asFormUrlEncoded().get("email")[0];

            try (Connection connection = DriverManager.getConnection(jdbc)) {
                String query = "INSERT INTO usuarios (name, email) VALUES (?,?);";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, name);
                    statement.setString(2, email);

                    statement.executeUpdate();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return internalServerError("Error al crear la base de datos");
        }

        return redirect(routes.HomeController.index());
    }
}