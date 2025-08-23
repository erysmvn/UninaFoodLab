    package GUI.Stages;

    import Controller.Controller;
    import Entity.*;
    import GUI.Pane.*;
    import GUI.Buttons.*;
    import javafx.geometry.Insets;
    import javafx.scene.*;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.*;
    import javafx.scene.control.*;
    import javafx.scene.paint.Color;
    import javafx.stage.*;

    public class AccountPage extends Stage {
        Controller controller;
        Scene scene;
        BorderPane root;
        StackPane content;

        Button impostazioniButton;
        Button accountButton;
        Button corsiButton;
        Button calendarButton;
        Button clickedButton;

        VBox impostazioniPanel;
        VBox accountPanel;
        BorderPane accountCorsiPanel;
        CalendarioPanel calendarioPanel;

        Utente utente;

        public AccountPage(Controller controller){

            this.controller = controller;
            this.initStyle(StageStyle.TRANSPARENT);

            root = createRoot();
            content = new StackPane();

            content.setStyle("-fx-background-color: WHITE;");

            scene = new Scene(root, 1050,750);
            scene.setFill(Color.TRANSPARENT);

            scene.setOnKeyPressed(event -> {
                if(event.isControlDown() && event.getCode() == KeyCode.W){
                    this.close();
                }
            });

            this.setScene(scene);
        }

        public void initPage(Utente utente){
            this.utente = utente;

            accountPanel = createAccountPanel(utente);
            accountCorsiPanel = createAccountCorsiPanel(controller);
            impostazioniPanel = new ImpostazioniPanel(controller);
            calendarioPanel = new CalendarioPanel(controller);
            calendarioPanel.initCalendario(utente);

            content.getChildren().addAll(accountPanel,accountCorsiPanel,impostazioniPanel,calendarioPanel);

            HBox topBar = createTopBar();
            root.setTop(topBar);
            root.setCenter(content);

            showOnlyPanel(accountPanel);
            setButtonAsActive(accountButton);
        }


        private Button createCalendarButton(){

            Button calendarButton = new Button("Calendario");
            setNotCLickedAesthetics(calendarButton);
            setFocusPropreties(calendarButton);
            setOnMouseTraverse(calendarButton, calendarioPanel);
            initButton(calendarButton,calendarioPanel);
            return calendarButton;
        }


        private void initButton(Button button, Pane panel) {
            setFocusPropreties(button);
            setOnMouseTraverse(button,panel);
            button.setOnAction(e -> {
                setButtonAsActive(button);
                showOnlyPanel(panel);
            });
        }

        private void setButtonAsActive(Button button) {
            clickedButton = button;

            if(clickedButton != accountButton) setNotCLickedAesthetics(accountButton);
            else setClickedAesthetics(accountButton);

            if(clickedButton != corsiButton) setNotCLickedAesthetics(corsiButton);
            else setClickedAesthetics(corsiButton);

            if(clickedButton != impostazioniButton) setNotCLickedAesthetics(impostazioniButton);
            else setClickedAesthetics(impostazioniButton);

            if(clickedButton != calendarButton) setNotCLickedAesthetics(calendarButton);
            else setClickedAesthetics(calendarButton);

        }

        private void setNotCLickedAesthetics(Button button) {
            button.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            button.setBorder(Border.EMPTY);
        }

        private void setClickedAesthetics(Button button) {
            button.setStyle("-fx-background-color: WHITE; -fx-text-fill: #3A6698;");
            button.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0, 0, 1, 0)
            )));
        }




        private BorderPane createRoot(){
            root = new BorderPane();
            root.setBackground(new Background(
                    new BackgroundFill(Color.WHITE, new CornerRadii(30, 30, 30, 30, false), Insets.EMPTY)
            ));

            root.setPadding(new Insets(10));
            root.setBackground(new Background(new BackgroundFill(
                    Color.WHITE, new CornerRadii(30, 30, 30, 30, false), Insets.EMPTY
            )));

            root.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(30,30,30,30,false),
                    new BorderWidths(2)
            )));
            return  root;
        }

        private VBox createAccountPanel(Utente utente){
            accountPanel = new AccountPanel(utente);
            accountPanel.setSpacing(10);
            accountPanel.setPadding(new Insets(10));
            accountPanel.setVisible(true);
            accountPanel.setManaged(true);
            return accountPanel;
        }

        private AccountCorsiPanel createAccountCorsiPanel( Controller controller){
            AccountCorsiPanel accountCorsiPanel = new AccountCorsiPanel(this.controller);
            accountCorsiPanel.initPanel(utente);
            return accountCorsiPanel;
        }


        private HBox createTopBar() {
            HBox topBar = new HBox();
            topBar.setPadding(new Insets(10));
            topBar.setSpacing(10);
            topBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            topBar.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0,0,2, 0)
            )));

            accountButton = createAccountButton();
            corsiButton = createCorsiButton();
            calendarButton = createCalendarButton();
            impostazioniButton = createImpostazioniButton();

            CircleButton minimizeBtn = new CircleButton().setToMinimizeButtonWithAction(this);
            CircleButton closeBtn = new CircleButton().setToCloseButtonWithAction(this);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            topBar.getChildren().addAll(accountButton, corsiButton, calendarButton, impostazioniButton, spacer, minimizeBtn, closeBtn);

            return  topBar;
        }

        private Button createAccountButton(){
            accountButton = new Button("Account");
            setClickedAesthetics(accountButton);
            initButton(accountButton,accountPanel);
            clickedButton = accountButton;
            return accountButton;
        }
        private Button createCorsiButton(){
            corsiButton = new Button("Corsi");
            setNotCLickedAesthetics(corsiButton);
            initButton(corsiButton,accountCorsiPanel);
            return corsiButton;
        }
        private Button createImpostazioniButton(){
            impostazioniButton = new Button("Impostazioni");
            setNotCLickedAesthetics(impostazioniButton);
            initButton(impostazioniButton,impostazioniPanel);
            return impostazioniButton;
        }

        private void setOnMouseTraverse(Button button, Pane panel) {
            button.setOnMouseEntered(e -> {
                if(clickedButton != button) setClickedAesthetics(button);
            });

            button.setOnMouseExited(e -> {
                if(clickedButton != button) setNotCLickedAesthetics(button);
            });
        }

        private void setFocusPropreties(Button button) {
            button.setFocusTraversable(true);
            button.focusedProperty().addListener((obs, oldValue, newValue) -> {
                if(clickedButton != button){
                    setNotCLickedAesthetics(button);
                }
            });
        }



        private void showOnlyPanel(Pane panelToShow) {
            Pane[] allPanels = {accountPanel, accountCorsiPanel, impostazioniPanel, calendarioPanel};

            for (Pane panel : allPanels) {
                if (panel == panelToShow) {
                    panel.setVisible(true);
                    panel.setManaged(true);
                } else {
                    panel.setVisible(false);
                    panel.setManaged(false);
                }
            }
        }

    }
