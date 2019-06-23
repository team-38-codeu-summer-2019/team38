import java.io.IOException;
import java.util.Map;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

@WebServlet("/my-form-handler")
public class FormHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the message entered by the user.
    String message = request.getParameter("message");

    // Get the URL of the image that the user uploaded to Blobstore.
    String imageUrl = getUploadedFileUrl(request, "image");

    // Output some HTML that shows the data the user entered.
    // A real codebase would probably store these in Datastore.
    ServletOutputStream out = response.getOutputStream();
    out.println("<p>Here's the image you uploaded:</p>");
    out.println("<a href=\"" + imageUrl + "\">");
    out.println("<img src=\"" + imageUrl + "\" />");
    out.println("</a>");
    out.println("<p>Here's the text you entered:</p>");
    out.println(message);
  }

  /**
<<<<<<< HEAD
   * Returns a URL that points to the uploaded file, or null if the user didn't
   * upload a file.
   */
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
=======
   * Returns a URL that points to the uploaded file, or null if the user didn't upload a file.
   */

  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName){
>>>>>>> 0296324df0e361f1f1d3f0e4110b944e8c5dcd40
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

<<<<<<< HEAD
    // User submitted form without selecting a file, so we can't get a URL.
    // (devserver)
    if (blobKeys == null || blobKeys.isEmpty()) {
=======
    // User submitted form without selecting a file, so we can't get a URL. (devserver)
    if(blobKeys == null || blobKeys.isEmpty()) {
>>>>>>> 0296324df0e361f1f1d3f0e4110b944e8c5dcd40
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

<<<<<<< HEAD
    // User submitted form without selecting a file, so we can't get a URL. (live
    // server)
=======
    // User submitted form without selecting a file, so we can't get a URL. (live server)
>>>>>>> 0296324df0e361f1f1d3f0e4110b944e8c5dcd40
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

<<<<<<< HEAD
    // We could check the validity of the file here, e.g. to make sure it's an image
    // file
=======
    // We could check the validity of the file here, e.g. to make sure it's an image file
>>>>>>> 0296324df0e361f1f1d3f0e4110b944e8c5dcd40
    // https://stackoverflow.com/q/10779564/873165

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    return imagesService.getServingUrl(options);
  }
}