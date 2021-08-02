package be.twofold.fcop.util;//

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;

public final class GifSequenceWriter {
    private final ImageWriter writer;
    private final ImageWriteParam param;
    private final IIOMetadata metadata;

    public GifSequenceWriter(
        ImageOutputStream output,
        int timeBetweenFramesMS,
        IndexColorModel model
    ) throws IOException {
        // my method to create a writer
        writer = ImageIO
            .getImageWritersByFormatName("gif")
            .next();

        param = writer.getDefaultWriteParam();

        metadata = writer.getDefaultImageMetadata(getImageTypeSpecifier(model), param);

        String metaFormatName = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

        IIOMetadataNode graphicControlExtension = getNode(root, "GraphicControlExtension");
        graphicControlExtension.setAttribute("disposalMethod", "none");
        graphicControlExtension.setAttribute("userInputFlag", "FALSE");
        graphicControlExtension.setAttribute("transparentColorFlag", "FALSE");
        graphicControlExtension.setAttribute("delayTime", String.valueOf(timeBetweenFramesMS / 10));
        graphicControlExtension.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode applicationExtension = new IIOMetadataNode("ApplicationExtension");
        applicationExtension.setAttribute("applicationID", "NETSCAPE");
        applicationExtension.setAttribute("authenticationCode", "2.0");
        applicationExtension.setUserObject(new byte[]{0x1, 0, 0});

        IIOMetadataNode applicationExtensions = getNode(root, "ApplicationExtensions");
        applicationExtensions.appendChild(applicationExtension);

        metadata.setFromTree(metaFormatName, root);
        writer.setOutput(output);
        writer.prepareWriteSequence(null);
    }

    private static IIOMetadataNode getNode(
        IIOMetadataNode rootNode,
        String nodeName
    ) {
        for (int i = 0; i < rootNode.getLength(); i++) {
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) rootNode.item(i);
            }
        }

        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return node;
    }

    private ImageTypeSpecifier getImageTypeSpecifier(IndexColorModel model) {
        byte[] r = new byte[model.getMapSize()];
        byte[] g = new byte[model.getMapSize()];
        byte[] b = new byte[model.getMapSize()];

        model.getReds(r);
        model.getGreens(g);
        model.getBlues(b);

        return ImageTypeSpecifier.createIndexed(r, g, b, null, 8, DataBuffer.TYPE_BYTE);
    }

    public void writeToSequence(RenderedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), param);
    }

    public void close() throws IOException {
        writer.endWriteSequence();
    }
}
