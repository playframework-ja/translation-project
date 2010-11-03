package controllers;

import play.Play;
import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void documentation() throws Exception {
		Documentation.page(
				Play.configuration.getProperty("version.latest"), "home");
	}

	public static void code() {
		render();
	}

	public static void ecosystem() {
		render();
	}

	public static void modules() {
		render();
	}
}