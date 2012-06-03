package helper;

import java.util.regex.*;

import org.pegdown.*;
import org.pegdown.ast.*;

/**
 * 
 * @author garbagetown
 * 
 */
public class GithubLinkRenderer extends LinkRenderer {

    private String path;

    /**
     * 
     * @param path
     */
    public GithubLinkRenderer(String path) {
        this.path = path;
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
            href = String.format("resources/%s/%s", path, text);
            text = String.format("<img src=%s>", href);
        } else {
            href = text;
        }
        return new LinkRenderer.Rendering(href, text);
    }
}
