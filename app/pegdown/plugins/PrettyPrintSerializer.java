package pegdown.plugins;

import org.apache.commons.lang.StringUtils;
import org.pegdown.Printer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.VerbatimNode;

public class PrettyPrintSerializer implements VerbatimSerializer {

    public static final PrettyPrintSerializer INSTANCE = new PrettyPrintSerializer();

    @Override
    public void serialize(VerbatimNode node, Printer printer) {
        printer.println().print("<pre class=\"prettyprint\"><code");
        if (!StringUtils.isEmpty(node.getType())) {
            printAttribute(printer, "class", node.getType());
        }
        printer.print(">");
        String text = node.getText();
        // print HTML breaks for all initial newlines
        while (text.charAt(0) == '\n') {
            printer.print("<br/>");
            text = text.substring(1);
        }
        printer.printEncoded(text);
        printer.print("</code></pre>");
    }

    private void printAttribute(final Printer printer, final String name, final String value) {
        printer.print(' ').print(name).print('=').print('"').print("language-").print(value).print('"');
    }
}
