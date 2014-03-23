package pegdown.plugins;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pegdown.LinkRenderer;
import org.pegdown.ast.WikiLinkNode;

/**
 * 
 * @author garbagetown
 * 
 */
public class GithubLinkRenderer extends LinkRenderer {

    private File page;

    /**
     * 
     * @param page
     */
    public GithubLinkRenderer(File page) {
        this.page = page;
    }

    @Override
    public Rendering render(WikiLinkNode node) {
        String text = node.getText();
        String href = "";
        if (text.contains("|")) {
            String[] parts = text.split(Pattern.quote("|"));
            text = parts[0].trim();
            href = parts[1].trim();
        } else if (text.endsWith(".png")) {
            String version = "";
            Matcher m = Pattern.compile("/([0-9¥¥.]+)/").matcher(page.getPath());
            if (m.find()) {
                version = m.group(1);
            }
            String docroot = String.format("documentation/%s/", version);
            String parent = page.getParent();
            String path = parent.substring(parent.indexOf(docroot) + docroot.length());
            href = String.format("resources/%s/%s", path, text);
            text = String.format("<img src=%s>", href);
        } else {
            href = text;
        }
        return new LinkRenderer.Rendering(href, text);
    }
}