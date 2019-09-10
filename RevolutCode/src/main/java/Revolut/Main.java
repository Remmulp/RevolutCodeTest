package Revolut;

import java.util.HashMap;
import java.util.Map;

import io.javalin.Javalin;

//This is class is used for testing the Javalin functionality
public class Main {
	static Map<String, String> reservations = new HashMap<String, String>() {{
        put("saturday", "No reservation");
        put("sunday", "No reservation");
    }};

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("‎⁨/public");
        }).start(8080);

        app.post("/make-reservation", ctx -> {
            reservations.put(ctx.formParam("day"), ctx.formParam("time"));
            ctx.html("Test has been saved");
        });

        app.get("/check-reservation", ctx -> {
            ctx.html(reservations.get(ctx.queryParam("day")));
        });

    }
}
