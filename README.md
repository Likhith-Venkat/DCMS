# DCMS Maker-Checker Service

This Spring Boot application provides endpoints for managing data objects with maker-checker functionality. It allows makers to add data objects and checkers to either reject or approve them.

## Endpoints

1. `/addobj`: Used by makers to add a data object. This endpoint accepts any object.
2. `/rejectobj`: Used by checkers to reject data objects. It accepts a JSON payload with properties: `id` (object id) and `rejectReason`.
3. `/approveobj`: Used by checkers to approve data objects. It accepts a JSON payload with property `id`.
4. `/get`: Used to fetch data based on `status` and `objectType`.

## Data Object Fields

Data objects have the following fields:

- `Object data`: The main data of the object.
- `String url`: The URL associated with the object.
- `Map<String, String> headers`: Additional headers for the object.
- `String objectType`: The type of the object.

Other fields include:

- `String method`: The HTTP method associated with the object.
- `String Username`: The username associated with the object.
- `String email`: The email associated with the object.
- `String created Date`: The date when the object was created.
- `String Status`: The status of the object.
- `String RejectReason`: The reason for rejection if applicable.

## Unit Testing

The project includes unit tests that require a local MongoDB server to run. Ensure that a MongoDB server is running locally before running the unit tests.

## Getting Started

To run the application:

1. Ensure you have Java and Gradle installed on your machine.
2. Clone this repository.
3. Navigate to the project directory.
4. Run `gradle bootRun` to start the application.

## Running Unit Tests

To run the unit tests:

1. Ensure you have a local MongoDB server running.
2. Navigate to the project directory.
3. Run `gradle test` to execute the unit tests.

## Running docker image

1. Install docker on your system.
2. Run `docker pull siddarthan11m2p/dcmsmc:v1`.
3. Once docker image is pulled run `docker run -it -p 8080:8080 siddarthan11m2p/dcmsmc:v1`.

## Additional Customizations

- You can customize the MongoDB connection URI in the `application.properties` file.

