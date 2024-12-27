# QR Code Generator Service

This project is a QR Code Generator Service built using Java, Spring Boot, and JavaScript. It allows users to generate QR codes with customizable content, size, error correction level, and image type.

## Features

- Generate QR codes with different error correction levels (L, M, Q, H).
- Customize the size of the QR code.
- Choose the image type (PNG, JPEG, GIF).
- Simple web interface for generating QR codes.
- REST API for generating QR codes programmatically.

## Technologies Used

- Java
- Spring Boot
- JavaScript
- ZXing (Zebra Crossing) library for QR code generation
- HTML/CSS for the web interface

## Getting Started

### Prerequisites

- Java 11 or higher
- Gradle
- A web browser

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/dauphongxd/QRCodeService.git
    cd QRCodeService
    ```

2. Build the project using Gradle:
    ```sh
    ./gradlew build
    ```

3. Run the Spring Boot application:
    ```sh
    ./gradlew bootRun
    ```

4. Open your web browser and navigate to `http://localhost:8080` to access the QR Code Generator web interface.

## Usage

### Web Interface

1. Enter the contents you want to encode in the QR code.
2. Select the size of the QR code (between 150 and 350 pixels).
3. Choose the error correction level (L, M, Q, H).
4. Select the image type (PNG, JPEG, GIF).
5. Click the "Generate QR Code" button to generate and display the QR code.

### REST API

You can also generate QR codes programmatically using the REST API.

#### Endpoint

- `GET /api/qrcode`

#### Parameters

- `contents` (required): The contents to encode in the QR code.
- `size` (optional): The size of the QR code (default is 250, must be between 150 and 350).
- `correction` (optional): The error correction level (default is L, options are L, M, Q, H).
- `type` (optional): The image type (default is PNG, options are png, jpeg, gif).

#### Example Request

```sh
curl -o qrcode.png "http://localhost:8080/api/qrcode?contents=Hello%20World&size=250&correction=M&type=png"