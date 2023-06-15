package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import play.i18n.MessagesApi;

import javax.inject.Inject;

public class HomeController extends Controller{

    private final Form<UserProcess> form;
    private MessagesApi messagesApi;

    @Inject
    public HomeController(FormFactory formFactory, MessagesApi messagesApi){
        this.form = formFactory.form(UserProcess.class);
        this.messagesApi = messagesApi;
    }

    public Result index(){
        return ok(views.html.index.render());
    }

    public Result listForm(Http.Request request){
        return ok(views.html.form.render(form, request, messagesApi.preferred(request)));
    }

    public Result createUser(Http.Request request){
        String name = request.body().asFormUrlEncoded().get("name")[0];
        String email = request.body().asFormUrlEncoded().get("email")[0];

        User user = new User();

        user.name = name;
        user.email = email;

        user.save();

        return redirect(routes.HomeController.index());
    }
}