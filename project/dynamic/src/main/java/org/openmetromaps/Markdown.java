package org.openmetromaps;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.openmetromaps.flexmark.CustomExtension;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import de.topobyte.jsoup.nodes.Element;

public class Markdown
{

	private static MutableDataSet options = new MutableDataSet();

	static {
		Extension tables = new TablesExtension() {

			@Override
			public void rendererOptions(MutableDataHolder options)
			{
				options.set(TablesExtension.CLASS_NAME, "table");
			}

		};

		Extension strikethrough = StrikethroughExtension.create();
		Extension wikilink = WikiLinkExtension.create();
		Extension custom = CustomExtension.create();

		options.set(Parser.EXTENSIONS,
				Arrays.asList(tables, strikethrough, custom, wikilink));
	}

	private static Parser parser()
	{
		return Parser.builder(options).build();
	}

	private static HtmlRenderer renderer()
	{
		return HtmlRenderer.builder(options).build();
	}

	public static void generate(Element container, String markdown)
	{
		Document document = parser().parse(markdown);
		String html = renderer().render(document);
		container.append(html);
	}

	public static void renderResource(Element content, String resource)
			throws IOException
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resource);
		String markdown = IOUtils.toString(input, StandardCharsets.UTF_8);

		generate(content, markdown);
	}

}
