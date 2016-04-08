package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.util.Base64;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.UnsupportedEncodingException;

/**
 * Created by raphaelseher on 16.11.15.
 */
public class ContentBlock9ViewHolderUtils {

  /**
   * Decodes a base64 string to an icon for mapMarkers.
   * Can handle normal image formats and also svgs.
   * The icon will be resized to width: 70, height will be resized to maintain imageRatio.
   *
   * @param base64String Base64 string that will be resized. Must start with "data:image/"
   * @return icon as BitMap, or null if there was a problem
   */
  public static Bitmap getIconFromBase64(String base64String, Fragment fragment) {
    Bitmap icon = null;
    byte[] data1;
    String decodedString1 = "";
    float newImageWidth = 25.0f;

    if (fragment.isAdded()) {
      //image will be resized depending on the density of the screen
      newImageWidth = newImageWidth * fragment.getResources().getDisplayMetrics().density;
    }

    if (base64String == null)
      return null;

    try {
      int index = base64String.indexOf("base64,");
      String decodedString1WithoutPrefix = base64String.substring(index + 7);
      data1 = Base64.decode(decodedString1WithoutPrefix, Base64.DEFAULT);
      decodedString1 = new String(data1, "UTF-8");

      if (base64String.contains("data:image/svg+xml")) {
        //svg stuff
        SVG svg = null;
        svg = SVG.getFromString(decodedString1);

        if (svg != null) {
          //resize svg
          float imageRatio = svg.getDocumentWidth() / svg.getDocumentHeight();
          svg.setDocumentWidth(newImageWidth);
          svg.setDocumentHeight(newImageWidth / imageRatio);

          icon = Bitmap.createBitmap((int) svg.getDocumentWidth(), (int) svg.getDocumentHeight(), Bitmap.Config.ARGB_8888);
          Canvas canvas1 = new Canvas(icon);
          svg.renderToCanvas(canvas1);
        }
      } else if (base64String.contains("data:image/")) {
        //normal image stuff
        icon = BitmapFactory.decodeByteArray(data1, 0, data1.length);
        //resize the icon
        double imageRatio = (double) icon.getWidth() / (double) icon.getHeight();
        double newHeight = newImageWidth / imageRatio;
        icon = Bitmap.createScaledBitmap(icon, (int) newImageWidth, (int) newHeight, false);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (SVGParseException e) {
      e.printStackTrace();
    }

    return icon;
  }
}
