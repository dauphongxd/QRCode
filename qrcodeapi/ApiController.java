package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.io.ByteArrayOutputStream;


@RestController
// This defines the ApiController class. The @RestController annotation indicates that this
// class is a controller where every method returns a domain object instead of a view.
// The @RequestMapping("/api") annotation maps all HTTP operations by default to the /api path.
@RequestMapping("/api")
public class ApiController{

    // This method creates a health check endpoint at /api/health. When accessed, it returns
    // an HTTP 200 OK status, indicating that the API is up and running.
    @GetMapping("/health")
    public ResponseEntity<String> getHealthStatus(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/qrcode")
    public ResponseEntity<?> getQRCode(@RequestParam(name = "size", required = false) Integer size,
                                       @RequestParam(name = "type", required = false) String type,
                                       @RequestParam(name = "correction", required = false) String correction,
                                       @RequestParam (name = "contents") String contents) {
        if (contents.isEmpty() || contents.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Contents cannot be null or blank"));
        }

        if (size == null) {
            size = 250;
        }

        if (size < 150 || size > 350) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Image size must be between 150 and 350 pixels"));
        }

        if (correction == null) {
            correction = "L";
        }

        ErrorCorrectionLevel errorCorrectionLevel;
        switch (correction.toUpperCase()) {
            case "L":
                errorCorrectionLevel = ErrorCorrectionLevel.L;
                break;
            case "M":
                errorCorrectionLevel = ErrorCorrectionLevel.M;
                break;
            case "Q":
                errorCorrectionLevel = ErrorCorrectionLevel.Q;
                break;
            case "H":
                errorCorrectionLevel = ErrorCorrectionLevel.H;
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Permitted error correction levels are L, M, Q, H"));
        }

        if (type == null) {
            type = "png";
        }

        if ((!type.equalsIgnoreCase("png")) && !type.equalsIgnoreCase("jpeg")
        && !type.equalsIgnoreCase("gif")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Only png, jpeg and gif image types are supported"));
        }


        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);

//            // Create image
//            BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = bufferedImage.createGraphics();
//            g.setColor(Color.WHITE);
//            g.fillRect(0,0,size,size);
//            g.dispose();

            // Convert Buffered Image to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, type, baos);
//            ImageIO.write(bufferedImage, type, baos);
            byte[] imageByte = baos.toByteArray();

            // Set appropriate Content-Type
            MediaType mediaType = switch (type.toLowerCase()){
                case "png" -> MediaType.IMAGE_PNG;
                case "jpeg" -> MediaType.IMAGE_JPEG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> throw new IllegalArgumentException("Unsupported image type");
            };

            return ResponseEntity.ok().contentType(mediaType).body(imageByte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((new ErrorResponse("An error occurred while processing the request")));
        }
    }

}


