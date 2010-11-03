package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import play.Play;
import play.libs.IO;
import play.mvc.Controller;

public class Documentation extends Controller {

	public static void page(String version, String id) throws Exception {

		String latest = Play.configuration.getProperty("version.latest");

		File page = new File(
				Play.applicationPath,
				"documentation/" + version + "/manual/" + id + ".textile");
		
		if (!page.exists()) {
			if (!version.equals(latest)) {
				page(latest, id);
			}
			notFound(page.getPath());
		}
		
		String textile = IO.readContentAsString(page);
		String html = toHTML(textile);
		String title = getTitle(textile);

		render(version, id, html, title);
	}

	static String toHTML(String textile) {
		String html = new MarkupParser(new TextileLanguage()).parseToHtml(textile);
		html = html.substring(html.indexOf("<body>") + 6, html.lastIndexOf("</body>"));
		return html;
	}

	static String getTitle(String textile) {
		if (textile.length() == 0) {
			return "";
		}
		return textile.split("\n")[0].substring(3).trim();
	}

}