# How to run the project:
---
### 1. Download zip:
Download the zip file from GitHub.
On **IntelliJ Code** go to: **File** > **New** > **Project from existing source..** > **path/to/project** > **Create project from existing source** > **Project format** should be `.idea` .
### 2. Import libraries:
Stored in folder `libraries` from project folder.
On **IntelliJ Code** go to: **File** > **Project Structure** > **Libraries** > Click on"**+**" button > **Java** > add the whole folder.
Apply and save.
### 3. Run configuration:
On **IntelliJ Code** go to: **Run** > **Edit configurations...** > **Libraries** > Click on"**+**" button > **Application** > **Modify options** > **Add VM options** > and add `--module-path /path/to/javafx-sdk-24.0.2/lib --add-modules javafx.controls,javafx.fxml there.
Apply and run `Main.java` file.
