package com.javing.steganograph.web;

import com.javing.steganograph.service.SteganographyService;
import com.javing.steganograph.service.UserInputValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

/**
 * Controller for the web interface of the steganography application.
 */
@Controller
@AllArgsConstructor
public class WebController {

    private final UserInputValidator userInputValidator;
    private final SteganographyService steganographyService;

    /**
     * Home page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Encode page - GET
     */
    @GetMapping("/encode")
    public String encodeForm() {
        return "encode";
    }

    /**
     * Encode page - POST
     * Processes the form submission to hide text in an image
     */
    @PostMapping("/encode")
    public String encodeSubmit(
            @RequestParam("text") String text,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            byte[] imageBytes = image.getBytes();
            userInputValidator.validate(imageBytes, text);

            byte[] processedImage = steganographyService.encodeMessage(text, imageBytes);

            // Store the processed image in the session for download
            redirectAttributes.addFlashAttribute("processedImage", processedImage);
            redirectAttributes.addFlashAttribute("originalFilename", image.getOriginalFilename());

            // Also store in the HTTP session for the download endpoint
            session.setAttribute("processedImage", processedImage);
            session.setAttribute("originalFilename", image.getOriginalFilename());

            return "redirect:/download";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error processing the image: " + e.getMessage());
            return "redirect:/encode";
        } catch (UserInputValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/encode";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/encode";
        }
    }

    /**
     * Download page for processed images
     */
    @GetMapping("/download")
    public String downloadPage(Model model) {
        if (!model.containsAttribute("processedImage")) {
            return "redirect:/";
        }

        // Convert the image to Base64 for display in the template
        byte[] processedImage = (byte[]) model.asMap().get("processedImage");
        String base64Image = Base64.getEncoder().encodeToString(processedImage);
        model.addAttribute("base64Image", base64Image);

        return "download";
    }

    /**
     * Decode page - GET
     */
    @GetMapping("/decode")
    public String decodeForm() {
        return "decode";
    }

    /**
     * Decode page - POST
     * Processes the form submission to extract text from an image
     */
    @PostMapping("/decode")
    public String decodeSubmit(
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            byte[] imageBytes = image.getBytes();
            userInputValidator.validate(imageBytes);

            String extractedText = steganographyService.decodeMessage(imageBytes);

            redirectAttributes.addFlashAttribute("extractedText", extractedText);
            return "redirect:/decode";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error processing the image: " + e.getMessage());
            return "redirect:/decode";
        } catch (UserInputValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/decode";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/decode";
        }
    }

    /**
     * Download file endpoint
     * Serves the processed image as a downloadable file
     */
    @GetMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile(HttpSession session) {
        byte[] processedImage = (byte[]) session.getAttribute("processedImage");
        String originalFilename = (String) session.getAttribute("originalFilename");

        if (processedImage == null || originalFilename == null) {
            return ResponseEntity.badRequest().build();
        }

        String filename = originalFilename.substring(0, originalFilename.lastIndexOf('.')) + "_steg.png";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(processedImage.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(processedImage);
    }
}
