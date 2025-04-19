# Testing the Steganography Application with cat.jpeg

The application runs on port 8081. Here are the curl commands to test it using the cat image found in the test/resources dir:

## 1. Hide Text in Image

```bash
curl -X POST \
  -F "image=@src/test/resources/cat.jpeg" \
  http://localhost:8081/steg/HelloCat \
  --output steg_cat_output.png
```

This command will:
- Take the cat.jpeg image from src/test/resources
- Hide the text "HelloCat" in it
- Save the resulting image as steg_cat_output.png in the current directory

## 2. Extract Text from Image

```bash
curl -X POST \
  -F "image=@steg_cat_output.png" \
  http://localhost:8081/desteg
```

This command will:
- Take the steg_cat_output.png image (created by the previous command)
- Extract the hidden text from it
- Display the extracted text in the terminal

## Notes

- Make sure the application is running on port 8081
- The text to hide must contain only letters, numbers, and spaces
- Run these commands from the project root directory
