package Revolut;

/**
 * Hello world!
 *
 */
public class JavalinApp {
    public static void main(String[] args) {
        SJavalin app = Javalin.create().port(7000).start();
        app.get("/hello", ctx -> ctx.html("Hello, Javalin!"));
    }
}
