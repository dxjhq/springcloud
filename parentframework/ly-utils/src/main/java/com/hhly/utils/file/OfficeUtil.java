package com.hhly.utils.file;

import com.hhly.utils.ValueUtil;
import com.hhly.utils.LogUtil;
import com.hhly.utils.encrypt.Encrypt;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/** word ==&gt; html */
public class OfficeUtil {
    private static final String HTML_SUFFIX = ".html";

    private static boolean is03Word(String fileName) {
        return ValueUtil.isNotBlank(fileName) && fileName.toLowerCase().endsWith("doc");
    }
    private static boolean is07Word(String fileName) {
        return ValueUtil.isNotBlank(fileName) && fileName.toLowerCase().endsWith("docx");
    }
    private static boolean notWord(String fileName) {
        return !is03Word(fileName) && !is07Word(fileName);
    }

    /**
     * 将 doc 或 docx 文档 转换成 html 文件
     *
     * @param inputStream 文件流
     * @param fileName    文件名称
     * @param path        存放地址
     * @param domain      跟存放地址能对应上的域名
     * @return 将可以被访问到的 url 地址返回
     */
    public static String word2Html ( InputStream inputStream, String fileName, String path, String domain ) {
//        U.assertNil( path, "上传文件要给一个存放的地方" );
//        U.assertNil( domain, "上传文件要给一个能被访问到的域名. 与存放地相对应" );
//        U.assertNil( fileName, "文件名为空" );
//        U.assertException( notWord( fileName ), "上传的不是 word 文件" );

        path = ValueUtil.addSuffix( path ) + FileUtil.DOC;
        domain = ValueUtil.addSuffix( domain ) + FileUtil.DOC;
        try {
            if ( is03Word( fileName ) ) {
                return office03( inputStream, path, domain );
            } else if ( is07Word( fileName ) ) {
                return office07( inputStream, path, domain );
            }
        } catch ( Exception e ) {
            if ( LogUtil.ROOT_LOG.isInfoEnabled() )
                LogUtil.ROOT_LOG.info( "convert exception", e );
        }
        return null;
    }
    private static String office03(InputStream file, final String path, final String domain)
            throws IOException, ParserConfigurationException, TransformerException {
        // 上传后的文件名, 如果文档中有图片, 将会以文件名进行 16 位的 md5 做为目录存放
        String       fileName = ValueUtil.uuid() + HTML_SUFFIX;
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        // word 中间的图片存放及显示在 html 中 <img src="hxxp://..." /> 的 url 值
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType,
                                      String suggestedName, float widthInches, float heightInches) {
                String dir = Encrypt.to16Md5(fileName);
                File parent = new File(ValueUtil.addSuffix(path) + dir);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                try (OutputStream os = new FileOutputStream(new File(parent, suggestedName))) {
                    os.write(content);
                } catch (IOException e) {
                    // ignore
                }
                return ValueUtil.addSuffix(domain) + ValueUtil.addSuffix(dir) + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(WordToHtmlUtils.loadDoc(file));
        // 源
        DOMSource source = new DOMSource(wordToHtmlConverter.getDocument());
        // 从源转换来的 html 文件
        StreamResult output = new StreamResult(new FileOutputStream(new File(path, fileName)));
        // doc 转换成 html
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.transform(source, output);

        /* try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            serializer.transform(new DOMSource(wordToHtmlConverter.getDocument()), new StreamResult(out));

            return new String(out.toByteArray());
        } */
        // 上面的存放目录与 domain 是能对应上的
        return ValueUtil.addSuffix(domain) + fileName;
    }
    private static String office07(InputStream file, String path, String domain) throws IOException {
        // 上传后的文件名, 如果文档中有图片, 将会以文件名进行 16 位的 md5 做为目录存放
        String fileName = ValueUtil.uuid() + HTML_SUFFIX;
        String dir = Encrypt.to16Md5(fileName);
        // 转换选项. 主要是设置图片存放及对应的 url 地址
        XHTMLOptions options = XHTMLOptions.create().URIResolver(new BasicURIResolver(ValueUtil.addSuffix(domain) + dir));
        options.setExtractor(new FileImageExtractor(new File(ValueUtil.addSuffix(path) + dir)));

        // 源
        XWPFDocument source = new XWPFDocument(file);
        // 从源转换来的 html 文件
        FileOutputStream output = new FileOutputStream(new File(path, fileName));
        // docx 转换成 html
        XHTMLConverter.getInstance().convert(source, output, options);

        /*try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XHTMLConverter.getInstance().convert(new XWPFDocument(file.getInputStream()), out, options);
            return new String(out.toByteArray());
        }*/
        // 上面的存放目录与 domain 是能对应上的
        return ValueUtil.addSuffix(domain) + fileName;
    }
}