# Steganography Application GUI

This document describes how to use the GUI for the Steganography Application.

## Overview

The Steganography Application now includes a web-based GUI built with Spring Boot and Thymeleaf. This GUI provides a user-friendly interface for hiding text in images and extracting hidden text from images.

## Features

- **Home Page**: Provides options to hide text in images or extract text from images
- **Hide Text**: Upload an image and enter text to hide within the image
- **Extract Text**: Upload an image to extract hidden text from it
- **Download**: Download the processed image with hidden text

## How to Use

1. Start the application (it runs on port 8081 by default)
2. Open a web browser and navigate to `http://localhost:8081`
3. From the home page, choose one of the following options:

### Hide Text in Image

1. Click on the "Hide Text" button on the home page
2. Enter the text you want to hide (only letters, numbers, and spaces are allowed)
3. Upload an image file
4. Click the "Hide Text" button
5. On the download page, you can preview the processed image and download it

### Extract Text from Image

1. Click on the "Extract Text" button on the home page
2. Upload an image that contains hidden text
3. Click the "Extract Text" button
4. The extracted text will be displayed on the page

## Technical Implementation

The GUI is implemented using:
- Spring Boot for the backend
- Thymeleaf for HTML templates
- CSS for styling
- No JavaScript required

The application uses the existing steganography services to process the images and text.

## Notes

- The text to hide must contain only letters, numbers, and spaces
- The application supports various image formats for input, but always outputs PNG files
- The GUI is responsive and works on both desktop and mobile browsers